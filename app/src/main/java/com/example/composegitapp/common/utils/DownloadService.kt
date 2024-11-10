package com.example.composegitapp.common.utils

import android.app.Notification
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.os.Build
import androidx.core.app.NotificationCompat
import okhttp3.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

class DownloadService : Service() {

    private val client = OkHttpClient()

    companion object {
        const val CHANNEL_ID = "DownloadServiceChannel"
        const val NOTIFICATION_ID = 333

        fun startService(context: Context, url: String, destinationFile: File) {
            val startIntent = Intent(context, DownloadService::class.java).apply {
                putExtra("url", url)
                putExtra("destinationFile", destinationFile.absolutePath)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(startIntent)
            } else {
                context.startService(startIntent)
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val url = intent?.getStringExtra("url") ?: return START_NOT_STICKY
        val destinationFilePath = intent.getStringExtra("destinationFile") ?: return START_NOT_STICKY
        val destinationFile = File(destinationFilePath)

        startForeground(NOTIFICATION_ID, createNotification("Starting download...", 0))

        downloadRepositoryZip(url, destinationFile)
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun createNotification(contentText: String, progress: Int): Notification {
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Downloading repository")
            .setContentText(contentText)
            .setSmallIcon(android.R.drawable.stat_sys_download)
            .setProgress(100, progress, false)

        return builder.build()
    }

    private fun updateNotification(progress: Int) {
        val notification = createNotification("Downloading... $progress%", progress)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    private fun downloadRepositoryZip(url: String, destinationFile: File) {
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                stopForeground(STOP_FOREGROUND_DETACH)
                stopSelf()
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    stopForeground(STOP_FOREGROUND_DETACH)
                    stopSelf()
                    return
                }

                response.body?.let { body ->
                    val inputStream: InputStream = body.byteStream()
                    val outputStream: OutputStream = FileOutputStream(destinationFile)
                    val buffer = ByteArray(2048)
                    var totalBytesRead = 0L
                    val fileSize = body.contentLength()

                    var bytesRead: Int
                    while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                        outputStream.write(buffer, 0, bytesRead)
                        totalBytesRead += bytesRead
                        val progress = ((totalBytesRead * 100) / fileSize).toInt()
                        updateNotification(progress)
                    }

                    outputStream.flush()
                    outputStream.close()
                    inputStream.close()
                }
                stopForeground(STOP_FOREGROUND_DETACH)
                stopSelf()
            }
        })
    }
}
