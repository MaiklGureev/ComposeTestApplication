package com.example.composegitapp.common.base

import androidx.paging.PagingSource
import androidx.paging.PagingState


interface IPagingSourceModel

interface IPagingSourceUseCase<IModel : IPagingSourceModel> {
    suspend fun loadData(query: String, page: Int): List<IModel>
}

abstract class AbstractPagingSource
<IUseCase : IPagingSourceUseCase<IModel>, IModel : IPagingSourceModel>(
    private val repository: IUseCase,
) : PagingSource<Int, IModel>() {

    private var query: String = ""
    private var page: Int = 1

    fun updateQueryAndResetPage(newQuery: String) {
        query = newQuery
        page = 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, IModel> {
        page = params.key ?: 1

        if (query.isEmpty()) return LoadResult.Page(
            data = emptyList(),
            prevKey = null,
            nextKey = null
        )

        return try {
            val response = repository.loadData(query, page)

            val nextPage = if (response.isEmpty()) {
                null
            } else {
                page + 1
            }

            return LoadResult.Page(
                data = response,
                prevKey = if (page == 1) null else page - 1,
                nextKey = nextPage
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, IModel>): Int {
        return 0
    }
}