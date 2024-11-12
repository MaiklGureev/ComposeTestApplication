package com.example.composegitapp.common.extension

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat

object ContextExt {

    fun Context.hideKeyboard() {
        val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow((this as? Activity)?.currentFocus?.windowToken, 0)
    }

    /**
     * Открывает указанный URL в браузере.
     *
     * @param url - URL-адрес, который необходимо открыть.
     */
    fun Context.openUrl(url: String) {
        try {
            val uri = if (url.startsWith("http://") || url.startsWith("https://")) {
                Uri.parse(url)
            } else {
                Uri.parse("https://$url")
            }

            val intent = Intent(Intent.ACTION_VIEW, uri)

            if (intent.resolveActivity(this.packageManager) != null) {
                this.startActivity(intent)
            } else {
                Toast.makeText(this, "No browser found to open the link", Toast.LENGTH_SHORT)
                    .show()
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Invalid URL", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }
}

fun Context.showLongToast(text: String) = Toast.makeText(this, text, Toast.LENGTH_LONG).show()

fun Context.openAppPermissionSettings() {
    this.showLongToast("Failed to save file. Check recording permission and upload again.")
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.parse("package:${this@openAppPermissionSettings.packageName}")
    }
    this.startActivity(intent)
}

fun Context.requestPermission(permission: String, launcher: ActivityResultLauncher<String>) {
    if (!hasPermission(permission)) {
        launcher.launch(permission)
    }
}

fun Context.hasPermission(permission: String): Boolean {
    return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
}