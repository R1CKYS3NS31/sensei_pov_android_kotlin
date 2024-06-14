package com.example.data.data.repository.pov

import com.example.data.common.result.PoVResult
import com.example.data.common.util.datasync.Syncable
import com.example.data.data.model.pov.NewPoV
import com.example.data.data.model.pov.PoV
import kotlinx.coroutines.flow.Flow

interface PoVRepository : Syncable {
    suspend fun addPoV(newPoV: NewPoV)
    suspend fun addEditPoV(newPoV: NewPoV)
    fun getAllPoVs(): Flow<PoVResult<List<PoV>>>
    fun getPoV(poVId: String): Flow<PoVResult<PoV>>
    suspend fun editPoV(poV: PoV): Flow<PoVResult<PoV>>
    suspend fun delete(poV: PoV)
}