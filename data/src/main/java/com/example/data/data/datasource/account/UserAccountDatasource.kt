package com.example.data.data.datasource.account

import android.util.Log
import com.example.data.common.dispatcher.Dispatcher
import com.example.data.common.dispatcher.PoVDispatchers
import com.example.data.common.dispatcher.di.ApplicationScope
import com.example.data.common.result.ErrorResponse
import com.example.data.common.result.PoVResult
import com.example.data.common.result.asPoVError
import com.example.data.common.result.asPoVResult
import com.example.data.common.util.datasync.Synchronizer
import com.example.data.data.model.account.UserAccount
import com.example.data.data.model.account.UserAccountSignIn
import com.example.data.data.model.account.UserAccountSignUp
import com.example.data.data.model.account.asEntity
import com.example.data.data.model.account.asRemoteModel
import com.example.data.data.model.account.asUserAccount
import com.example.data.data.repository.account.UserAccountRepository
import com.example.datastore.UserAccountPreferencesRepository
import com.example.local.dao.account.UserAccountDao
import com.example.local.entity.account.UserAccountEntity
import com.example.remote.api.account.UserAccountApiService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

const val TAG = "UserAccountDatasource"

class UserAccountDatasource @Inject constructor(
    private val userAccountDao: UserAccountDao, // local datasource
    private val userAccountApiService: UserAccountApiService, // remote datasource
    @Dispatcher(PoVDispatchers.IO) private val dispatcher: CoroutineDispatcher,
    @ApplicationScope private val applicationCoroutineScope: CoroutineScope,
    private val userAccountPreferencesRepository: UserAccountPreferencesRepository
) : UserAccountRepository {
    //    override val isOnline: StateFlow<Boolean> = networkMonitor.isOnline
//        .map { it }
//        .stateIn(
//            scope = applicationCoroutineScope,
//            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
//            initialValue = false
//        )

    /* isAuthenticated */
    override val authToken: StateFlow<String?> = userAccountPreferencesRepository.getToken().map {
        it
    }.stateIn(
        scope = applicationCoroutineScope,
        started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
        initialValue = null
    )

    override fun getAllUserAccounts(): Flow<PoVResult<List<UserAccount>>> {
        return userAccountDao.getAllUserAccounts().map {
            it.map(UserAccountEntity::asUserAccount)
        }.asPoVResult()
    }

    override suspend fun getUserCredentials(): Flow<Pair<String?, String?>> =
        userAccountPreferencesRepository.getUserAccountCredentials().catch { it.asPoVError() }
            .flowOn(dispatcher)

    override suspend fun signUp(userAccountSignUp: UserAccountSignUp): Flow<PoVResult<UserAccount>> {
        return flow {
            emit(PoVResult.Loading)
            flowOf(userAccountApiService.signup(userAccountSignUp.asRemoteModel())).asPoVResult()
                .map { response ->
                    when (response) {
                        is PoVResult.Success -> {
                            val userAccountRemote = response.data.userAccountRemoteModel
                            response.data.let {
                                saveUserAccountToDatastore(
                                    id = it.userAccountRemoteModel.id,
                                    password = it.userAccountRemoteModel.password,
                                    token = it.token
                                )
//                                authInterceptorRepository.updateToken(tokenString = it.token)
                            }
                            userAccountDao.insertUserAccount(userAccountRemote.asEntity())
                            emit(PoVResult.Success(userAccountRemote.asUserAccount()))
                        }

                        is PoVResult.Error -> {
                            Log.d(
                                TAG,
                                "sign up error: ${response.responseErrorMessage}",
                                response.throwable
                            )
                            emit(
                                PoVResult.Error(
                                    throwable = response.throwable,
                                    responseErrorMessage = response.responseErrorMessage
                                )
                            )
                        }

                        is PoVResult.Loading -> emit(PoVResult.Loading)
                    }
                }.collect()
        }.catch { throwable ->
            emit(throwable.asPoVError())
        }.flowOn(dispatcher)
    }

    override suspend fun signIn(userAccountSignIn: UserAccountSignIn): Flow<PoVResult<UserAccount>> =
        flow {
            emit(PoVResult.Loading)
            flowOf(userAccountApiService.signIn(userAccountSignIn.asRemoteModel())).asPoVResult()
                .map { response ->
                    when (response) {
                        is PoVResult.Success -> {
                            val userAccountRemoteModel = response.data.userAccountRemoteModel
                            response.let {
                                saveUserAccountToDatastore(
                                    id = it.data.userAccountRemoteModel.id,
                                    password = it.data.userAccountRemoteModel.password,
                                    token = it.data.token
                                )
//                        authInterceptorRepository.updateToken(it.token)
                            }/* save user account to room */
                            userAccountDao.upsertUserAccount(userAccountRemoteModel.asEntity())
                            emit(PoVResult.Success(userAccountRemoteModel.asUserAccount()))
                        }

                        is PoVResult.Error -> {
                            emit(
                                PoVResult.Error(
                                    throwable = response.throwable,
                                    responseErrorMessage = response.responseErrorMessage
                                )
                            )
                        }

                        is PoVResult.Loading -> {
                            emit(PoVResult.Loading)
                        }
                    }
                }.collect()
        }.catch {
            emit(it.asPoVError())
        }.flowOn(dispatcher)

    override suspend fun signOut(userAccount: UserAccount): Flow<PoVResult<Unit>> = flow {
        emit(PoVResult.Loading)
        userAccountPreferencesRepository.signout()
        flowOf(userAccountApiService.signOut()).asPoVResult().map { response: PoVResult<Unit> ->
                when (response) {
                    is PoVResult.Success -> {
                        emit(PoVResult.Success(response.data))
                    }

                    is PoVResult.Error -> {
                        Log.d(
                            TAG,
                            "sign out error: ${response.responseErrorMessage}",
                            response.throwable
                        )
                        emit(
                            PoVResult.Error(
                                throwable = response.throwable,
                                responseErrorMessage = response.responseErrorMessage
                            )
                        )
                    }

                    is PoVResult.Loading -> {
                        emit(PoVResult.Loading)
                    }
                }
            }.collect()
        userAccountDao.deleteUserAccount(userAccountEntity = userAccount.asEntity())
        emit(PoVResult.Success(Unit))
    }.catch { it.asPoVError() }.flowOn(dispatcher)

    override suspend fun getUserAccount(userAccountId: String): Flow<PoVResult<UserAccount>> =
        flow {
            emit(PoVResult.Loading)

            userAccountDao.getUserAccount(id = userAccountId).asPoVResult().map { response ->
                    when (response) {
                        is PoVResult.Success -> {
                            val userAccountEntity = response.data
                            if (userAccountEntity.email.isNotBlank()) {
                                emit(PoVResult.Success(userAccountEntity.asUserAccount()))
                            }
                        }

                        is PoVResult.Error -> {
                            emit(
                                PoVResult.Error(
                                    throwable = response.throwable,
                                    responseErrorMessage = response.responseErrorMessage
                                )
                            )
                        }

                        is PoVResult.Loading -> {
                            emit(PoVResult.Loading)
                        }
                    }
                }
        }

    override suspend fun updateUserAccount(userAccount: UserAccount): Flow<PoVResult<UserAccount>> {
        return flow {
            emit(PoVResult.Loading)
            val userAuthToken = authToken.map {
                it
            }.first()
            if (userAuthToken?.isNotBlank() == true) {
                flowOf(
                    userAccountApiService.updateUserAccount(
                        userAccount.asRemoteModel()
                    )
                ).asPoVResult().map { response ->
                    when (response) {
                        is PoVResult.Success -> {
                            userAccountDao.updateUserAccount(
                                response.data.asEntity()
                            )
                            emit(PoVResult.Success(response.data.asUserAccount()))
                        }

                        is PoVResult.Error -> {
                            emit(
                                PoVResult.Error(
                                    throwable = response.throwable,
                                    responseErrorMessage = response.responseErrorMessage
                                )
                            )
                        }

                        is PoVResult.Loading -> emit(PoVResult.Loading)
                    }
                }.collect()
                userAccountDao.updateUserAccount(userAccount.asEntity())
                userAccountDao.getUserAccount(userAccount.id).asPoVResult()
            } else {
                emit(
                    PoVResult.Error(
                        responseErrorMessage = ErrorResponse(
                            errorCode = 401, errorMessage = "unauthorized - please sign in"
                        )
                    )
                )
            }
        }.catch {
            emit(it.asPoVError())
        }.flowOn(dispatcher)
    }

    override suspend fun deleteUserAccount(userAccount: UserAccount): Flow<PoVResult<Unit>> {
        return flow {
            emit(PoVResult.Loading)
            val userAuthToken = authToken.map { it }.first()

            if (userAuthToken?.isNotBlank() == true) {

                /* remove user account from room*/
                userAccountDao.deleteUserAccount(userAccountEntity = userAccount.asEntity())
                emit(PoVResult.Success(Unit))
            }
        }.catch {
            emit(it.asPoVError())
        }.flowOn(dispatcher)
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    private suspend fun saveUserAccountToDatastore(
        id: String, password: String, token: String
    ) {
        userAccountPreferencesRepository.saveUserAccountCredential(id, password)
        userAccountPreferencesRepository.saveToken(token)
    }

    override suspend fun syncWith(synchronizer: Synchronizer): Boolean {
        TODO("Not yet implemented")
    }
}