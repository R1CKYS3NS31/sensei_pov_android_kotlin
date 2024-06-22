package com.example.local.dao.pov

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.example.local.entity.pov.PoVEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PoVDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPoV(poVEntity: PoVEntity)

    @Upsert
    suspend fun upsertPoV(poVEntity: PoVEntity)

    @Query("select * from pov")
    fun getAllPoVs(): Flow<List<PoVEntity>>

    @Query("select * from pov where id =:id")
    fun getPoV(id: String): Flow<PoVEntity>

    @Update
    suspend fun updatePoV(poVEntity: PoVEntity)

    @Delete
    suspend fun deletePoV(poVEntity: PoVEntity)
}