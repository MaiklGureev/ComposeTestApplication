package com.example.composegitapp.common.utils.downloads

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.composegitapp.App
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.io.InputStream
import javax.inject.Inject

class DownloadService : Service() {


    @Inject
    lateinit var client: OkHttpClient

    @Inject
    lateinit var mediaStoreUtils: MediaStoreUtils

    @Inject
    lateinit var downloadManager: IDownloadManager

    override fun onCreate() {
        super.onCreate()
        // Perform the injection
        (applicationContext as App).appComponent.inject(this)

        // Create notification channel if on Android 8.0 or above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Download Service",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Channel for download notifications"
            }

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val url = intent?.getStringExtra(URL_KEY) ?: return START_NOT_STICKY
        val fileName = intent.getStringExtra(FILE_NAME) ?: return START_NOT_STICKY

        // Start the service in the foreground with a valid notification
        startForeground(NOTIFICATION_ID, createNotification("Starting download...", 0))

        // Download the file and save it using MediaStoreUtils
        downloadRepositoryZip(url, fileName)

        // Service should not be restarted if killed
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun createNotification(contentText: String, progress: Int): Notification {
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Downloading repository")
            .setContentText(contentText)
            .setSmallIcon(android.R.drawable.stat_sys_download)
            .setProgress(100, progress, false)
            .setOngoing(true) // Ensures the notification stays visible and persistent
            .setPriority(NotificationCompat.PRIORITY_LOW)

        return builder.build()
    }

    private fun updateNotification(progress: Int) {
        val notification = createNotification("Downloading... $progress%", progress)
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    private fun downloadRepositoryZip(url: String, fileName: String) {
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Stop foreground when download fails and stop the service
                stopForeground(STOP_FOREGROUND_DETACH)
                stopSelf()
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    stopForeground(STOP_FOREGROUND_DETACH)
                    stopSelf()
                    return
                }

                try {
                    // Get the input stream from the response body
                    response.body?.let { body ->
                        val inputStream: InputStream = body.byteStream()

                        // Write the file using MediaStoreUtils
                        val fileUri = mediaStoreUtils.writeFile(fileName, inputStream)

                        // If the file was successfully written, update the notification
                        if (fileUri != null) {
                            updateNotification(100)
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    downloadManager.sendEventError()
                }

                stopForeground(STOP_FOREGROUND_DETACH)
                stopSelf()
            }
        })
    }

    companion object {
        const val CHANNEL_ID = "DownloadServiceChannel"
        const val NOTIFICATION_ID = 333

        private const val URL_KEY = "URL_KEY"
        private const val FILE_NAME = "FILE_NAME"

        fun startService(context: Context, url: String, fileName: String) {
            val startIntent = Intent(context, DownloadService::class.java).apply {
                putExtra(URL_KEY, url)
                putExtra(FILE_NAME, fileName)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(startIntent)
            } else {
                context.startService(startIntent)
            }
        }
    }
}
