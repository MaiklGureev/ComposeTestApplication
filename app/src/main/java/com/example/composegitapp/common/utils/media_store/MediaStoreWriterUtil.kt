package com.example.composegitapp.common.utils.media_store

import android.Manifest
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.example.composegitapp.common.utils.downloads.IDownloadManager
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

class MediaStoreWriterUtil @Inject constructor(
    private val downloadManager: IDownloadManager,
    private val contentResolver: ContentResolver,
    private val context: Context
) {

    /**
     * Writes a downloaded file to the Downloads folder using MediaStore API.
     */
    fun writeFile(
        fileName: String,
        inputStream: InputStream,
        updateNotification: (Boolean) -> Unit
    ): Uri? {
        val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Handle Android 10+ devices with MediaStore
            writeFileToDownloadsWithMediaStore(fileName, inputStream, updateNotification)
        } else {
            // Handle pre-Android 10 devices (legacy approach)
            writeFileToLegacyDownloads(fileName, inputStream, updateNotification)
        }
        uri?.let {
            downloadManager.sendEventDownloaded()
        }
        return uri
    }


    /**
     * For Android 10 (API 29) and above: use MediaStore API
     */
    @RequiresApi(Build.VERSION_CODES.Q)
    private fun writeFileToDownloadsWithMediaStore(
        fileName: String,
        inputStream: InputStream,
        updateNotification: (Boolean) -> Unit
    ): Uri? {

        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, "application/zip")
            put(
                MediaStore.MediaColumns.RELATIVE_PATH,
                Environment.DIRECTORY_DOWNLOADS
            )
        }

        val contentUri: Uri = MediaStore.Downloads.EXTERNAL_CONTENT_URI
        val fileUri: Uri? = contentResolver.insert(contentUri, contentValues)
        val fileSize: Long = inputStream.available().toLong()

        try {
            fileUri?.let { uri ->
                val outputStream: OutputStream? = contentResolver.openOutputStream(uri)
                outputStream?.use { output ->
                    val buffer = ByteArray(4096)
                    var bytesRead: Int
                    var totalBytesWritten: Long = 0
                    while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                        output.write(buffer, 0, bytesRead)
                        totalBytesWritten += bytesRead

                        val progress = calculateProgress(totalBytesWritten, fileSize)
                        //todo not worked
                    }
                    updateNotification(true)
                    output.flush()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            downloadManager.sendEventError()
        }

        return fileUri
    }

    /**
     * For Android versions before Android 10 (legacy approach):
     * Write the file directly to the Download directory.
     */
    private fun writeFileToLegacyDownloads(
        fileName: String,
        inputStream: InputStream,
        updateNotification: (Boolean) -> Unit
    ): Uri? {
        // Check if permission is granted to write to external storage
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            downloadManager.sendEventPermissionError()
            throw IllegalStateException("Permission denied: WRITE_EXTERNAL_STORAGE")
        }

        // Define the file path for legacy Android versions
        val downloadDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val file = File(downloadDir, fileName)
        val fileSize: Long = inputStream.available().toLong()

        try {
            // Create file if it doesn't exist
            if (!file.exists()) {
                file.createNewFile()
            }

            // Write to file
            val outputStream = FileOutputStream(file)
            inputStream.use { input ->
                outputStream.use { output ->
                    val buffer = ByteArray(4096)
                    var bytesRead: Int
                    var totalBytesWritten: Long = 0
                    while (input.read(buffer).also { bytesRead = it } != -1) {
                        output.write(buffer, 0, bytesRead)
                        totalBytesWritten += bytesRead
                    }
                    updateNotification(true)
                    output.flush()
                }
            }

            return Uri.fromFile(file)
        } catch (e: IOException) {
            downloadManager.sendEventError()
            e.printStackTrace()
            return null
        }
    }

    private fun calculateProgress(totalBytesWritten: Long, fileSize: Long) =
        ((totalBytesWritten * 100).safeDivide(fileSize)).toInt()

    private fun Long.safeDivide(divisor: Long): Long {
        return if (divisor != 0L) {
            this / divisor
        } else {
            0L
        }
    }
}