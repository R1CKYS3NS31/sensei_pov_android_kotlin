package com.example.data.data.datasource.pov

import android.util.Log
import com.example.data.common.dispatcher.Dispatcher
import com.example.data.common.dispatcher.PoVDispatchers
import com.example.data.common.dispatcher.di.ApplicationScope
import com.example.data.common.result.PoVResult
import com.example.data.common.result.asPoVError
import com.example.data.common.result.asPoVResult
import com.example.data.common.util.datasync.Synchronizer
import com.example.data.common.util.datasync.dataSynchronizer
import com.example.data.data.model.pov.NewPoV
import com.example.data.data.model.pov.PoV
import com.example.data.data.model.pov.asEntity
import com.example.data.data.model.pov.asPoV
import com.example.data.data.model.pov.asRemote
import com.example.data.data.repository.pov.PoVRepository
import com.example.local.dao.pov.PoVDao
import com.example.local.entity.pov.PoVEntity
import com.example.remote.api.pov.PoVApiService
import com.example.remote.model.pov.PoVRemoteModel
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

const val TAG = "PoVDatasource"

class PoVDatasource @Inject constructor(
    private val poVDao: PoVDao, // local source
    private val poVApiService: PoVApiService, // remote source
    @Dispatcher(PoVDispatchers.IO)
    private val dispatcher: CoroutineDispatcher,
    @ApplicationScope
    private val applicationScope: CoroutineScope
) : PoVRepository {
//    override suspend fun addPoV(newPoV: NewPoV): Flow<PoVResult<PoV>> {
//        return flow {
//            emit(PoVResult.Loading)
//            val localPoV = poVDao.insertPoV(newPoV.asPoV().asEntity()).asPoV()
//            val remotePoV = poVApiService.createPoV(newPoV.asRemote()).asPoV()
//
//        }
//    }

    override suspend fun addPoV(newPoV: NewPoV): Flow<PoVResult<PoV>> {
        return flow {
            emit(PoVResult.Loading)
            poVDao.insertPoV(newPoV.asPoV().asEntity())
            flowOf(poVApiService.createPoV(newPoV.asRemote())).asPoVResult()
                .map { response ->
                    when (response) {
                        is PoVResult.Success -> {
                            val povRemote = response.data
                            poVDao.insertPoV(povRemote.asPoV().asEntity())
                            emit(PoVResult.Success(povRemote.asPoV()))
                        }

                        is PoVResult.Error -> {
                            Log.d(
                                TAG,
                                "create PoV error: ${response.responseErrorMessage}",
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
        }.catch { throwable: Throwable ->
            emit(throwable.asPoVError())
        }.flowOn(dispatcher)
    }

    override suspend fun addEditPoV(newPoV: NewPoV): Flow<PoVResult<PoV>> {
        TODO("Not yet implemented")
    }

    override fun getAllPoVs(): Flow<PoVResult<List<PoV>>> {
        return flow {
            emit(PoVResult.Loading)
            flowOf(poVApiService.readPoVs()).asPoVResult()
                .map { response ->
                    when (response) {
                        is PoVResult.Success -> {
                            val povRemoteModels = response.data
                            povRemoteModels.map {
                                poVDao.upsertPoV(it.asPoV().asEntity())
                            }
                            emit(PoVResult.Success(povRemoteModels.map(PoVRemoteModel::asPoV)))
                        }

                        is PoVResult.Error -> {
                            Log.d(
                                TAG,
                                "get PoVs error: ${response.responseErrorMessage}",
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
            poVDao.getAllPoVs().map { it.map(PoVEntity::asPoV) }.asPoVResult().collect()
        }.catch {
            emit(it.asPoVError())
        }.flowOn(dispatcher)
    }

    override fun getPoV(poVId: String): Flow<PoVResult<PoV>> {
        return flow {
            emit(PoVResult.Loading)
            flowOf(poVApiService.readPoV(poVId)).asPoVResult()
                .map { response ->
                    when (response) {
                        is PoVResult.Success -> {
                            val poVRemote = response.data
                            poVDao.updatePoV(poVRemote.asPoV().asEntity())
                            emit(PoVResult.Success(poVRemote.asPoV()))
                        }

                        is PoVResult.Error -> {
                            Log.d(
                                TAG,
                                "get PoV error: ${response.responseErrorMessage}",
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
            poVDao.getPoV(poVId).map { it.asPoV() }.asPoVResult()
        }.catch {
            emit(it.asPoVError())
        }.flowOn(dispatcher)
    }

    override suspend fun editPoV(poV: PoV): Flow<PoVResult<PoV>> {
        return flow {
            emit(PoVResult.Loading)
            flowOf(poVApiService.updatePoV(poV.asRemote())).asPoVResult()
                .map { response ->
                    when (response) {
                        is PoVResult.Success -> {
                            val poVRemote = response.data
                            poVDao.updatePoV(poVRemote.asPoV().asEntity())
                            emit(PoVResult.Success(poVRemote.asPoV()))
                        }

                        is PoVResult.Error -> {
                            Log.d(
                                TAG,
                                "update PoV error: ${response.responseErrorMessage}",
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
            poVDao.getPoV(poV.id).map { it.asPoV() }.asPoVResult()
        }.catch {
            emit(it.asPoVError())
        }.flowOn(dispatcher)
    }

    override suspend fun delete(poV: PoV): Flow<PoVResult<PoV>> {
        return flow {
            emit(PoVResult.Loading)
            flowOf(poVApiService.deletePoV(poV.id)).asPoVResult()
                .map { response ->
                    when (response) {
                        is PoVResult.Success -> {
                            val povRemoteDeleted = response.data
                            poVDao.deletePoV(povRemoteDeleted.asPoV().asEntity())
                            emit(PoVResult.Success(povRemoteDeleted.asPoV()))
                        }

                        is PoVResult.Error -> {
                            Log.d(
                                TAG,
                                "delete PoV error: ${response.responseErrorMessage}",
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
            poVDao.getPoV(poV.id).map { it.asPoV() }.asPoVResult()
        }.catch {
            emit(it.asPoVError())
        }.flowOn(dispatcher)
    }

    override suspend fun syncWith(synchronizer: Synchronizer): Boolean {
        return synchronizer.dataSynchronizer(
            remoteData = poVApiService.readPoVs().map(PoVRemoteModel::asPoV).toSet(),
            localData = poVDao.getAllPoVs().first().map(PoVEntity::asPoV).toSet(),
            localModelUpdater = {
                poVDao.upsertPoV(it.asEntity())
            }
        )
    }
}