package com.example.composegitapp.ui.screen_repo_branches

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composegitapp.clean_arch_comp.domain.models.RepoBranchItemDomain
import com.example.composegitapp.clean_arch_comp.domain.use_case.IGetUserRepoBranchesUseCase
import com.example.composegitapp.common.utils.downloads.IDownloadManager
import com.example.composegitapp.ui.design_system.DownloadStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class UserRepoBranchesViewModel @Inject constructor(
    private val getUserRepoBranchesUseCase: IGetUserRepoBranchesUseCase,
    private val downloadManager: IDownloadManager
) : ViewModel() {

    private val _uiState = mutableStateOf(UserRepoBranchesUiModel())
    val uiState: State<UserRepoBranchesUiModel> = _uiState

    val eventDownloads: SharedFlow<IDownloadManager.DownloadEvents> = downloadManager.eventFlow

    init {
        viewModelScope.launch {
            eventDownloads.collect { event ->
                when (event) {
                    is IDownloadManager.DownloadEvents.Downloaded -> updateItemState(
                        event.task.id,
                        DownloadStatus.Downloaded
                    )

                    is IDownloadManager.DownloadEvents.Error -> updateItemState(
                        event.task.id,
                        DownloadStatus.NoDownloaded
                    )

                    is IDownloadManager.DownloadEvents.PermissionError -> updateItemState(
                        event.task.id,
                        DownloadStatus.NoDownloaded
                    )

                    IDownloadManager.DownloadEvents.Empty -> Unit
                }
            }
        }
    }

    fun onDownloadIconClicked(
        item: UserRepoBranchesUiModel.RepoBranchViewModel,
        userName: String,
        repoName: String
    ) {
        viewModelScope.launch(Dispatchers.Main) {
            updateItemState(item.id, DownloadStatus.Progress)
            downloadManager.downloadFile(item.id, item.url, "${userName}-${repoName}-${item.name}")
        }
    }

    private fun updateItemState(id: Int, status: DownloadStatus) {
        _uiState.value = _uiState.value.copy(
            repoBranchList = _uiState.value.repoBranchList.map {
                if (it.id == id) {
                    it.copy(downloadStatus = status)
                } else {
                    it
                }
            },
            isLoading = false
        )
    }

    fun getRepoBranches(userName: String, userRepo: String) {
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
        viewModelScope.launch {
            try {
                val repoBranches =
                    getUserRepoBranchesUseCase.loadData(userName, userRepo).map { it.mapToUi() }
                _uiState.value = _uiState.value.copy(
                    repoBranchList = repoBranches,
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

    data class UserRepoBranchesUiModel(
        val repoBranchList: List<RepoBranchViewModel> = emptyList(),
        val isLoading: Boolean = false,
        val errorMessage: String? = null
    ) {
        data class RepoBranchViewModel(
            val id: Int,
            val name: String,
            val url: String,
            val downloadStatus: DownloadStatus = DownloadStatus.NoDownloaded,
        )
    }

    private fun RepoBranchItemDomain.mapToUi(): UserRepoBranchesUiModel.RepoBranchViewModel {
        return UserRepoBranchesUiModel.RepoBranchViewModel(
            id = id,
            name = name,
            url = url
        )
    }


}

