package com.example.composegitapp.clean_arch_comp.domain.models

import android.net.Uri
import java.io.File

data class FileItemDomain(
    val id: Int,
    val fileName: String,
    val file: File,
    val uri: Uri
)