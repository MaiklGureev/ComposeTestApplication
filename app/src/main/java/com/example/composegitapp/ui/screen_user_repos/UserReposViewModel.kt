package com.example.composegitapp.ui.screen_user_repos

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composegitapp.clean_arch_comp.domain.models.UserRepoItemDomain
import com.example.composegitapp.clean_arch_comp.domain.use_case.IGetUserReposUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class UserReposViewModel @Inject constructor(
    private val searchUserReposUseCase: IGetUserReposUseCase
) : ViewModel() {

    private val _uiState = mutableStateOf(SearchUserUiModel())
    val uiState: State<SearchUserUiModel> = _uiState

    fun searchRepos(userName: String) {
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
        viewModelScope.launch {
            try {
                val repos = searchUserReposUseCase.loadData(userName)
                _uiState.value = _uiState.value.copy(
                    repoList = repos.map { it.mapToUi() },
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Failed to load repositories: ${e.localizedMessage}"
                )
            }
        }
    }

    data class SearchUserUiModel(
        val repoList: List<UserRepoUiModel> = emptyList(),
        val isLoading: Boolean = false,
        val errorMessage: String? = null
    ) {
        data class UserRepoUiModel(
            val id: Int,
            val author: String,
            val name: String,
            val description: String,
            val fullName: String,
            val url: String
        )
    }

    private fun UserRepoItemDomain.mapToUi(): SearchUserUiModel.UserRepoUiModel {
        return SearchUserUiModel.UserRepoUiModel(
            id = id,
            author = author,
            description = description,
            name = name,
            fullName = fullName,
            url = url
        )
    }
}

