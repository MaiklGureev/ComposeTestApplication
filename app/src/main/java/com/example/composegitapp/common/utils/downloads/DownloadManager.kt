package com.example.composegitapp.common.utils.downloads

import android.content.Context
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.runBlocking
import java.util.LinkedList
import java.util.Queue
import javax.inject.Inject

class DownloadManager @Inject constructor(
    val context: Context,
) : IDownloadManager {

    private val _eventFlow: MutableSharedFlow<IDownloadManager.DownloadEvents> = MutableSharedFlow()
    override val eventFlow: SharedFlow<IDownloadManager.DownloadEvents> = _eventFlow

    private var isDownloading = false

    private var currentTask: IDownloadManager.DownloadTask? = null
    private val downloadQueue: Queue<IDownloadManager.DownloadTask> = LinkedList()

    override fun sendEventError() {
        runBlocking {
            currentTask?.let {
                _eventFlow.emit(IDownloadManager.DownloadEvents.Error(it))
            }
            isDownloading = false
            processDownloadQueue()
        }
    }

    override fun sendEventPermissionError() {

        runBlocking {
            currentTask?.let {
                _eventFlow.emit(IDownloadManager.DownloadEvents.PermissionError(it))
            }
            isDownloading = false
            processDownloadQueue()
        }
    }

    override fun sendEventDownloaded() {
        runBlocking {
            currentTask?.let {
                _eventFlow.emit(IDownloadManager.DownloadEvents.Downloaded(it))
            }
            isDownloading = false
            processDownloadQueue()
        }
    }

    override suspend fun downloadFile(
        id: Int,
        url: String,
        fileName: String
    ) {
        val task = IDownloadManager.DownloadTask(id = id, url = url, fileName = fileName)
        downloadQueue.offer(task)

        // Если загрузка не активна, начинаем обработку очереди
        if (!isDownloading) {
            processDownloadQueue()
        }
    }

    private fun processDownloadQueue() {
        if (isDownloading) {
            return
        }
        if (downloadQueue.isEmpty()) {
            isDownloading = false
            currentTask = null
            return
        }

        val task = downloadQueue.poll()

        task?.let {
            currentTask = task
            isDownloading = true
            DownloadService.startService(context, it.url, it.fileName)
        }
    }
}

interface IDownloadManager {
    val eventFlow: SharedFlow<DownloadEvents>

    fun sendEventError()
    fun sendEventPermissionError()
    fun sendEventDownloaded()

    suspend fun downloadFile(id: Int, url: String, fileName: String)

    data class DownloadTask(
        val id: Int,
        val url: String,
        val fileName: String
    )

    sealed class DownloadEvents() {
        data object Empty : DownloadEvents()
        data class Error(val task: DownloadTask) : DownloadEvents()
        data class PermissionError(val task: DownloadTask) : DownloadEvents()
        data class Downloaded(val task: DownloadTask) : DownloadEvents()
    }
}