package com.example.composegitapp.ui.screen_downloads

import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composegitapp.clean_arch_comp.domain.models.FileItemDomain
import com.example.composegitapp.common.utils.media_store.MediaStoreReaderUtil
import com.example.composegitapp.common.utils.media_store.ZipUtils
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

class RepoDownloadsViewModel @Inject constructor(
    private val mediaStoreReaderUtil: MediaStoreReaderUtil,
    private val zipUtils: ZipUtils
) : ViewModel() {

    private val _uiState = mutableStateOf(RepoDownloadsUiModel())
    val uiState: State<RepoDownloadsUiModel> = _uiState

    fun onItemClicked(
        item: RepoDownloadsUiModel.RepoDownloadViewModel,
    ) {
        viewModelScope.launch {
            zipUtils.openZipFile(
                zipFileName = item.name.dropLast(4),
                fileItem = item.file,
                uriItem = item.uri,
                showLoader = ::showLoader,
                showError = ::showError
            )
        }
    }

    private fun showLoader(showLoader: Boolean) {
        _uiState.value = _uiState.value.copy(
            isLoading = showLoader,
        )
    }

    private fun showError(error: String) {
        _uiState.value = _uiState.value.copy(
            errorMessage = error,
        )
    }

    fun getRepoDownloads() {
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
        viewModelScope.launch {
            try {
                val repoBranches =
                    mediaStoreReaderUtil.getFileList().map { it.mapToUi() }
                _uiState.value = _uiState.value.copy(
                    repoDownloadsList = repoBranches,
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

    data class RepoDownloadsUiModel(
        val repoDownloadsList: List<RepoDownloadViewModel> = emptyList(),
        val isLoading: Boolean = false,
        val errorMessage: String? = null
    ) {
        data class RepoDownloadViewModel(
            val id: Int,
            val name: String,
            val file: File,
            val uri: Uri,
        )
    }

    private fun FileItemDomain.mapToUi(): RepoDownloadsUiModel.RepoDownloadViewModel {
        return RepoDownloadsUiModel.RepoDownloadViewModel(
            id = id,
            name = fileName,
            file = file,
            uri = uri
        )
    }


}

