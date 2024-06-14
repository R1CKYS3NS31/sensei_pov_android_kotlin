package com.example.data.data.datasource.pov

import com.example.data.common.dispatcher.Dispatcher
import com.example.data.common.dispatcher.PoVDispatchers
import com.example.data.common.dispatcher.di.ApplicationScope
import com.example.data.common.result.PoVResult
import com.example.data.common.result.asPoVResult
import com.example.data.common.util.datasync.Synchronizer
import com.example.data.data.model.pov.NewPoV
import com.example.data.data.model.pov.PoV
import com.example.data.data.model.pov.asPoV
import com.example.data.data.model.pov.asPoVEntity
import com.example.data.data.repository.pov.PoVRepository
import com.example.local.dao.pov.PoVDao
import com.example.local.entity.pov.PoVEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PoVDatasource @Inject constructor(
    private val poVDao: PoVDao, //
    @Dispatcher(PoVDispatchers.IO)
    private val dispatcher: CoroutineDispatcher,
    @ApplicationScope
    private val applicationScope: CoroutineScope
) : PoVRepository {
    override suspend fun addPoV(newPoV: NewPoV) {
        poVDao.insertPoV(newPoV.asPoV().asPoVEntity())
    }

    override suspend fun addEditPoV(newPoV: NewPoV) {
        poVDao.upsert(newPoV.asPoV().asPoVEntity())
    }

    override fun getAllPoVs(): Flow<PoVResult<List<PoV>>> {
        return poVDao.getAllPoVs().map {
            it.map(PoVEntity::asPoV)
        }.asPoVResult()
    }

    override fun getPoV(poVId: String): Flow<PoVResult<PoV>> {
        return poVDao.getPoV(poVId).map(PoVEntity::asPoV).asPoVResult()
    }

    override suspend fun editPoV(poV: PoV): Flow<PoVResult<PoV>> {
        poVDao.updatePoV(poV.asPoVEntity())
        return poVDao.getPoV(poV.id).map(PoVEntity::asPoV).asPoVResult()
    }

    override suspend fun delete(poV: PoV) {
        poVDao.deletePoV(poV.asPoVEntity())
    }

    override suspend fun syncWith(synchronizer: Synchronizer): Boolean {
        TODO("Not yet implemented")
    }
}