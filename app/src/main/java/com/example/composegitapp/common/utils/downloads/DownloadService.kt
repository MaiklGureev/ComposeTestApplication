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
import com.example.composegitapp.common.utils.media_store.MediaStoreWriterUtil
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
    lateinit var mediaStoreWriterUtil: MediaStoreWriterUtil

    @Inject
    lateinit var downloadManager: IDownloadManager

    @Inject
    lateinit var notificationManager: NotificationManager

    override fun onCreate() {
        super.onCreate()
        // Perform the injection
        (applicationContext as App).appComponent.inject(this)

        // Create notification channel if on Android 8.0 or above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Download Service",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Channel for download notifications"
            }

            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val url = intent?.getStringExtra(URL_KEY) ?: return START_NOT_STICKY
        val fileName = intent.getStringExtra(FILE_NAME) ?: return START_NOT_STICKY

        // Start the service in the foreground with a valid notification
        startForeground(NOTIFICATION_ID, createNotification("Starting download..."))

        // Download the file and save it using MediaStoreUtils
        downloadRepositoryZip(url, fileName)

        // Service should not be restarted if killed
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun createNotification(
        contentText: String,
    ): Notification {
        val builder = NotificationCompat.Builder(this, CHANNEL_ID).apply {
            setContentTitle("Downloading repository")
            setContentText(contentText)
            setSound(null)
            setSmallIcon(android.R.drawable.stat_sys_download)
            setOngoing(true) // Ensures the notification stays visible while ongoing
            setPriority(NotificationCompat.PRIORITY_LOW)
        }

        return builder.build()
    }

    private fun updateNotification(isCompleted: Boolean) {

        val notification = if (isCompleted) {
            createNotification("Download complete!")
        } else {
            createNotification("Downloading... ")
        }

        // Update the notification
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    private fun downloadRepositoryZip(url: String, fileName: String) {
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Stop foreground when download fails and stop the service
                stopService()
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    stopService()
                    return
                }

                try {
                    // Get the input stream from the response body
                    response.body?.let { body ->
                        val inputStream: InputStream = body.byteStream()

                        // Write the file using MediaStoreUtils
                        mediaStoreWriterUtil.writeFile(
                            fileName = fileName,
                            inputStream = inputStream,
                            updateNotification = ::updateNotification
                        )

                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    downloadManager.sendEventError()
                }

                stopService()
            }
        })
    }

    private fun stopService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            stopForeground(STOP_FOREGROUND_DETACH)
        } else {
            stopForeground(true)  // Для устройств с API <= 23
        }
        stopSelf()
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
