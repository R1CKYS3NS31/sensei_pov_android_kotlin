package com.example.data.data.datasource.search

import com.example.data.common.dispatcher.Dispatcher
import com.example.data.common.dispatcher.PoVDispatchers
import com.example.data.data.model.pov.asPoV
import com.example.data.data.model.search.SearchResult
import com.example.data.data.repository.search.SearchRepository
import com.example.local.dao.pov.PoVDao
import com.example.local.dao.pov.PoVFtsDao
import com.example.local.entity.pov.PoVEntity
import com.example.local.entity.pov.asFtsEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.withContext
import javax.inject.Inject

const val TAG = "search_datasource"

internal class SearchDatasource @Inject constructor(
    private val poVDao: PoVDao,
    private val poVFtsDao: PoVFtsDao,
    @Dispatcher(PoVDispatchers.IO) private val ioDispatcher: CoroutineDispatcher
) : SearchRepository {
    override suspend fun populateFtsData() {
        withContext(ioDispatcher) {
            poVFtsDao.insertAll(
                poVDao.getAllPoVs().first().map(PoVEntity::asFtsEntity)
            )
        }
    }

    override fun search(searchQuery: String): Flow<SearchResult> {
        val povIds = poVFtsDao.searchAllPoVs("*$searchQuery*")

        val poVFlow = povIds.mapLatest {
            it.toSet()
        }.distinctUntilChanged()
            .flatMapLatest(PoVDao::getPoVs)

//        return combine(poVFlow){ poVs ->
//            SearchResult(povs = poVs.map{
//                it
//            }
//        }
    }

    override fun getSearchCount(): Flow<Int> =
        poVFtsDao.getCount()

}