package com.example.data.data.datasource.user

import android.util.Log
import com.example.data.common.dispatcher.Dispatcher
import com.example.data.common.dispatcher.PoVDispatchers
import com.example.data.common.dispatcher.di.ApplicationScope
import com.example.data.common.result.PoVResult
import com.example.data.common.result.asPoVError
import com.example.data.common.result.asPoVResult
import com.example.data.common.util.datasync.Synchronizer
import com.example.data.common.util.datasync.dataSynchronizer
import com.example.data.data.model.user.User
import com.example.data.data.model.user.asUser
import com.example.data.data.model.user.asUserEntity
import com.example.data.data.repository.user.UserRepository
import com.example.local.dao.user.UserDao
import com.example.local.entity.user.UserEntity
import com.example.remote.api.user.UserApiService
import com.example.remote.model.user.UserRemoteModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

const val TAG = "userDatasource"

class UserDatasource @Inject constructor(
    private val userDao: UserDao,
    private val userApiService: UserApiService,
    @ApplicationScope
    private val applicationCoroutineScope: CoroutineScope,
    @Dispatcher(PoVDispatchers.IO)
    private val dispatcher: CoroutineDispatcher
) : UserRepository {
    override suspend fun getUser(userId: String): Flow<PoVResult<User>> {
        return flow {
            emit(PoVResult.Loading)
            flowOf(userApiService.getUser(userId))
                .asPoVResult().map { response ->
                    when (response) {
                        is PoVResult.Success -> {
                            val userRemoteModel = response.data
                            userDao.updateUser(userRemoteModel.asUserEntity())
                            emit(PoVResult.Success(userRemoteModel.asUser()))
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
            userDao.getUser(userId).map {
                it.asUser()
            }.asPoVResult()
        }.catch {
            emit(it.asPoVError())
        }.flowOn(dispatcher)
    }

    override fun getAllUsers(): Flow<PoVResult<List<User>>> {
        return flow {
            emit(PoVResult.Loading)
            flowOf(userApiService.getAllUsers())
                .asPoVResult()
                .map { response ->
                    when (response) {
                        is PoVResult.Success -> {
                            val userRemoteModels = response.data
                            userRemoteModels.map {
                                userDao.upsertUser(it.asUserEntity())
                            }
                            emit(
                                PoVResult.Success(
                                    userRemoteModels
                                        .map(UserRemoteModel::asUser)
                                )
                            )
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

            /* from local */
            userDao.getAllUsers().asPoVResult()
                .map { response -> // unnecessary
                    when (response) {
                        is PoVResult.Success -> {
                            Log.d(TAG, response.toString())
                            val users = response.data.map(UserEntity::asUser)
                            emit(PoVResult.Success(users))
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
    }

    override suspend fun syncWith(synchronizer: Synchronizer): Boolean {
        return synchronizer.dataSynchronizer(
            remoteData = userApiService.getAllUsers().map(UserRemoteModel::asUser).toSet(),
            localData = userDao.getAllUsers().first().map(UserEntity::asUser).toSet(),
            localModelUpdater = {
                userDao.upsertUser(it.asUserEntity())
            },
        )
    }
}