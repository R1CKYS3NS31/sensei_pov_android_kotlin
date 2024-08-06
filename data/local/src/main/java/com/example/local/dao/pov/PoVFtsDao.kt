package com.example.local.dao.pov

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.local.entity.pov.PoVFtsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PoVFtsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(poVs: List<PoVFtsEntity>)

    @Query("select id from povfts where povfts match :query")
    fun searchAllPoVs(query: String): Flow<List<String>>

    @Query("select count(*) from povfts")
    fun getCount(): Flow<Int>
}