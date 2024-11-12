package com.example.composegitapp.common.utils.media_store

import android.content.ContentResolver
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import com.example.composegitapp.clean_arch_comp.domain.models.FileItemDomain
import com.example.composegitapp.common.preferences.IAppSettings
import java.io.File
import javax.inject.Inject

class MediaStoreReaderUtil @Inject constructor(
    private val contentResolver: ContentResolver,
    private val appSettings: IAppSettings,
) {

    fun getFileList(): List<FileItemDomain> {
        return readFiles().filter { it.fileName.contains(appSettings.getAppName()) }
    }

    private fun readFiles(): List<FileItemDomain> {
        val files = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Handle Android 10+ devices with MediaStore and Scoped Storage
            getZipFilesFromDownloadsScoped()
        } else {
            // Handle pre-Android 10 devices (legacy approach)
            getZipFilesFromDownloadsLegacy().map {
                FileItemDomain(id = it.hashCode(), fileName = it.name, file = it, uri = it.toUri())
            }
        }
        println(files)
        return files
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun getZipFilesFromDownloadsScoped(): List<FileItemDomain> {
        // URI для всех файлов в MediaStore
        val collection = MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL)

        // Столбцы, которые мы хотим получить
        val projection = arrayOf(
            MediaStore.Files.FileColumns.DISPLAY_NAME,
            MediaStore.Files.FileColumns.MIME_TYPE,
            MediaStore.Files.FileColumns.RELATIVE_PATH,
            MediaStore.Files.FileColumns.SIZE,
            MediaStore.Files.FileColumns.DATE_MODIFIED,
            MediaStore.Files.FileColumns._ID
        )

        // Фильтрация для получения только файлов с расширением .zip в папке Downloads
        val selection = """
        ${MediaStore.Files.FileColumns.RELATIVE_PATH} LIKE ? 
        AND ${MediaStore.Files.FileColumns.DISPLAY_NAME} LIKE ?
    """
        val selectionArgs = arrayOf(
            "${Environment.DIRECTORY_DOWNLOADS}%",  // Папка Downloads
            "%.zip"  // Фильтр по расширению .zip
        )

        // Запрос к ContentProvider для получения файлов
        val cursor = contentResolver.query(
            collection,
            projection,
            selection,
            selectionArgs,
            "${MediaStore.Files.FileColumns.DATE_MODIFIED} DESC"  // Сортировка по дате модификации
        )

        val zipFiles = mutableListOf<FileItemDomain>()

        cursor?.use { c ->
            val displayNameColumn = c.getColumnIndex(MediaStore.Files.FileColumns.DISPLAY_NAME)
            val relativePathColumn = c.getColumnIndex(MediaStore.Files.FileColumns.RELATIVE_PATH)
            val idColumn = c.getColumnIndex(MediaStore.Files.FileColumns._ID)

            while (c.moveToNext()) {
                val id = c.getString(idColumn)
                val displayName = c.getString(displayNameColumn)
                val relativePath = c.getString(relativePathColumn)

                // Получаем URI для файла
                val uri = Uri.withAppendedPath(
                    collection,
                    id
                )

                // Используем contentResolver для получения потока данных, а не конвертируем в File
                val inputStream = contentResolver.openInputStream(uri)
                if (inputStream != null) {
                    // Мы не можем использовать обычный File здесь, так как доступ к файлам происходит через ContentProvider
                    val file = File(uri.toString())
                    val domainFile = FileItemDomain(
                        id = displayName.hashCode(),
                        fileName = displayName,
                        file = file,
                        uri = uri
                    )
                    zipFiles.add(domainFile)  // Можно добавить обработку URI
                }
            }
        }

        return zipFiles
    }

    // Для Android до API 29
    private fun getZipFilesFromDownloadsLegacy(): List<File> {
        val downloadsDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val zipFiles = mutableListOf<File>()

        // Получаем список всех файлов в папке Downloads
        val files = downloadsDir.listFiles()

        // Фильтруем только .zip файлы
        files?.forEach { file ->
            if (file.isFile && file.name.endsWith(".zip")) {
                zipFiles.add(file)
            }
        }

        return zipFiles
    }
}