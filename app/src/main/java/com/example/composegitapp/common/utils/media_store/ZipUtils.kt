package com.example.composegitapp.common.utils.media_store

import android.content.ActivityNotFoundException
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import com.example.composegitapp.common.preferences.AppSettings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.util.Locale
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import javax.inject.Inject

class ZipUtils @Inject constructor(
    private val context: Context,
    private val contentResolver: ContentResolver,
    private val appSettings: AppSettings
) {
    fun openFileViewer(file: File) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val uri = Uri.fromFile(file)
        intent.setDataAndType(uri, getMimeType(file.extension))
        context.startActivity(intent)
    }

    suspend fun openZipFile(
        zipFileName: String,
        fileItem: File,
        uriItem: Uri,
        showLoader: suspend (Boolean) -> Unit,
        showError: suspend (String) -> Unit
    ) = withContext(Dispatchers.IO) {
        try {
            showLoader.invoke(true)
            val targetDirectory = extractZip(fileItem, uriItem)
            openDirectoryInFileManager(targetDirectory, zipFileName)
        } catch (e: ActivityNotFoundException) {
            e.printStackTrace()
            showError.invoke("Установите файловый менеджер и попробуйте снова.")
        } catch (e: Throwable) {
            e.printStackTrace()
            showError.invoke(e.localizedMessage.orEmpty())
        } finally {
            showLoader.invoke(false)
        }
    }

    private suspend fun extractZip(zipFileUri: File, uriItem: Uri): File =
        withContext(Dispatchers.IO) {
            val targetDirectory = createTargetDirectory(zipFileUri, uriItem)

            val buffer = ByteArray(1024)

            // В зависимости от версии Android, используем разные способы доступа к файлам
            return@withContext if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // Scoped Storage (Android 10 и выше)
                extractZipScoped(uriItem, targetDirectory, buffer)
            } else {
                // Старый способ для Android 9 и ниже
                extractZipOld(zipFileUri, targetDirectory, buffer)
            }
        }

    @RequiresApi(Build.VERSION_CODES.Q)
    private suspend fun extractZipScoped(
        zipFileUri: Uri,
        targetDirectory: File,
        buffer: ByteArray
    ): File {
        // Открываем входной поток для чтения zip-файла из Scoped Storage
        val zipInputStream = contentResolver.openInputStream(zipFileUri)
            ?: throw FileNotFoundException("Unable to open file at URI: $zipFileUri")

        val zipStream = ZipInputStream(zipInputStream)

        var zipEntry: ZipEntry?
        var newFileUri: Uri? = null
        while (zipStream.nextEntry.also { zipEntry = it } != null) {
            val newFile = File(targetDirectory, zipEntry!!.name)

            if (zipEntry!!.isDirectory) {
                // Если это директория, создаем ее
                newFile.mkdirs()
            } else {
                // Создаем новый файл в Scoped Storage
                newFileUri = createFileInScopedStorage(targetDirectory, zipEntry!!.name)
                val outputStream = contentResolver.openOutputStream(newFileUri)
                    ?: throw IOException("Unable to open output stream for: $newFileUri")

                try {
                    var totalBytesWritten: Long = 0

                    outputStream.use { output ->
                        var len: Int
                        // Чтение данных из zip-архива и запись в новый файл
                        while (zipStream.read(buffer).also { len = it } > 0) {
                            output.write(buffer, 0, len)
                            totalBytesWritten += len
                        }
                        output.flush()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    // Закрываем outputStream после использования
                    outputStream.close()
                }
            }

            zipStream.closeEntry()
        }
        zipStream.close()
        return newFileUri?.path?.let { File(it) }
            ?: throw FileNotFoundException("Unable to create files at URI: $targetDirectory")
    }

    // Реализация для Android 9 и ниже (старый способ)
    private suspend fun extractZipOld(
        zipFileUri: File,
        targetDirectory: File,
        buffer: ByteArray
    ): File {
        // Здесь предполагаем, что zipFileUri это путь к файлу в обычной файловой системе
        val zipFile = File(zipFileUri.path ?: throw FileNotFoundException("File path is null"))
        val zipInputStream = ZipInputStream(FileInputStream(zipFile))

        var zipEntry: ZipEntry?
        while (zipInputStream.nextEntry.also { zipEntry = it } != null) {
            val newFile = File(targetDirectory, zipEntry!!.name)
            if (zipEntry!!.isDirectory) {
                newFile.mkdirs()
            } else {
                FileOutputStream(newFile).use { outputStream ->
                    var len: Int
                    while (zipInputStream.read(buffer).also { len = it } > 0) {
                        outputStream.write(buffer, 0, len)
                    }
                }
            }
            zipInputStream.closeEntry()
        }
        zipInputStream.close()

        return targetDirectory
    }

    // Метод для создания файла в Scoped Storage
    @RequiresApi(Build.VERSION_CODES.Q)
    private fun createFileInScopedStorage(targetDirectory: File, fileName: String): Uri {
        // Создаем новый файл через ContentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(
                MediaStore.MediaColumns.MIME_TYPE,
                "application/octet-stream"
            ) // Подставьте правильный MIME тип
            put(
                MediaStore.MediaColumns.RELATIVE_PATH,
                Environment.DIRECTORY_DOWNLOADS + "/" + appSettings.getAppName() + "/" + fileName
            )
        }

        val contentUri = MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        val fileUri = contentResolver.insert(contentUri, contentValues)
            ?: throw IOException("Unable to create file")

        return fileUri
    }

    private fun openDirectoryInFileManager(directory: File, zipFileName: String) {
        val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val uri = directory.convertToContentDir(zipFileName)
            Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, DocumentsContract.Document.MIME_TYPE_DIR)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
        } else {

            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                directory
            )

            Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, DocumentsContract.Document.MIME_TYPE_DIR)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
        }

        // Запускаем активность для открытия директории
        context.startActivity(intent)
    }


    private fun getMimeType(format: String): String {
        return when (format.lowercase(Locale.getDefault())) {
            "pdf" -> "application/pdf"
            "jpg", "jpeg" -> "image/jpeg"
            "png" -> "image/png"
            "txt" -> "text/plain"
            "zip" -> "application/zip"
            else -> "*/*"
        }
    }

    private suspend fun createTargetDirectory(
        zipFileUri: File,
        uriItem: Uri
    ): File =
        withContext(Dispatchers.IO) {
            return@withContext if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // Для Android 10+ (API 29 и выше) используем Scoped Storage
                createTargetDirectoryScoped(uriItem)
            } else {
                // Для старых версий Android (до API 29) используем обычную файловую систему
                createTargetDirectoryOld(zipFileUri)
            }
        }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun createTargetDirectoryScoped(zipFileUri: Uri): File {
        // Получаем родительскую директорию (например, Downloads или Documents)
        val parentDirectory = contentResolver.query(
            zipFileUri,
            arrayOf(MediaStore.MediaColumns.RELATIVE_PATH),
            null,
            null,
            null
        )?.use {
            if (it.moveToFirst()) {
                it.getString(it.getColumnIndex(MediaStore.MediaColumns.RELATIVE_PATH) ?: -1)
            } else {
                null
            }
        }
        // Если родительская директория есть, создаем нужную директорию
        val targetDirectoryPath = parentDirectory + "/" + appSettings.getAppName()
        val targetDirectory = File(targetDirectoryPath)

        if (!targetDirectory.exists()) {
            targetDirectory.mkdirs()
        }

        return targetDirectory
    }

    private fun createTargetDirectoryOld(zipFileUri: File): File {
        // Для старых версий (API 28 и ниже), получаем путь из FileUri
        val parentDirectory = zipFileUri.parentFile?.absolutePath
            ?: throw FileNotFoundException("Parent directory is null")

        val targetDirectory = File(parentDirectory, appSettings.getAppName())
        if (!targetDirectory.exists()) {
            targetDirectory.mkdirs()
        }

        return targetDirectory
    }

    private fun File.convertToContentDir(zipFileName: String): Uri {
        // Используем MediaStore.Files для работы с файлами
        val collectionUri = MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL)

        // Указываем правильный путь, который будет относительным для MediaStore
        val relativePath =
            "${Environment.DIRECTORY_DOWNLOADS}/${appSettings.getAppName()}/$zipFileName"

        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, zipFileName) // Имя файла
            put(MediaStore.MediaColumns.RELATIVE_PATH, relativePath) // Путь к файлу в директории
            put(MediaStore.MediaColumns.MIME_TYPE, "application/octet-stream") // MIME тип
        }
        // Вставляем в MediaStore и получаем URI для файла
        return contentResolver.insert(collectionUri, contentValues) ?: Uri.EMPTY
    }

}

