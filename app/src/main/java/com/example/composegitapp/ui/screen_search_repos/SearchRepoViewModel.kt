package com.example.composegitapp.ui.screen_search_repos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.composegitapp.clean_arch_comp.domain.models.RepoItemDomain
import com.example.composegitapp.network.NetworkParams
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

class SearchRepoViewModel @Inject constructor(
    private val repoPagingSource: Provider<ReposPagingSource>
) : ViewModel() {
    private val _isRefreshing = MutableStateFlow(false)
    private val _query = MutableStateFlow("")
    private val _pagingQuery = MutableStateFlow("")

    val isRefreshing: StateFlow<Boolean> get() = _isRefreshing.asStateFlow()
    val query: StateFlow<String> get() = _query

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val items: Flow<PagingData<RepoUiModel>> = _pagingQuery
        .debounce(2000)
        .flatMapLatest { query ->
            createNewPagingData(query)
        }
        .cachedIn(viewModelScope)


    fun onQueryChange(newQuery: String) {
        _query.value = newQuery
        if (newQuery.isNotBlank() && newQuery.length >= NetworkParams.MIN_QUERY_LENGTH) {
            _pagingQuery.value = _query.value
        }
    }

    fun onSearch() {
        _query.value = _query.value
        _pagingQuery.value = _query.value
    }


    fun refresh() {
        viewModelScope.launch {
            _isRefreshing.emit(true)
            _pagingQuery.value = ""
            delay(100)
            _pagingQuery.value = _query.value
            delay(500)
            _isRefreshing.emit(false)
        }
    }

    private fun createNewPagingData(query: String): Flow<PagingData<RepoUiModel>> {
        return Pager(
            PagingConfig(pageSize = NetworkParams.PER_PAGE_DEFAULT_VALUE)
        ) {
            repoPagingSource.get().apply {
                updateQueryAndResetPage(query)
            }
        }.flow.map { pagingData -> pagingData.map { it.convertToUI() } }.cachedIn(viewModelScope)
    }

    data class RepoUiModel(
        val author: String,
        val name: String,
        val description: String,
        val htmlUrl: String,
    )

    private fun RepoItemDomain.convertToUI(): RepoUiModel {
        return RepoUiModel(
            name = name,
            author = author,
            description = description,
            htmlUrl = htmlUrl
        )
    }


}