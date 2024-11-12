package com.example.composegitapp.ui.screen_menu

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.composegitapp.common.extension.requestPermission
import com.example.composegitapp.common.extension.showLongToast
import com.example.composegitapp.ui.NavigationScreens
import com.example.composegitapp.ui.screen_menu.widgets.MenuContent

@Composable
fun MenuScreen(
    navHostController: NavHostController,
) {
    val onSearchClicked = { navHostController.navigate(NavigationScreens.SearchRepoScreen.name) }
    val onDownloadsClicked =
        { navHostController.navigate(NavigationScreens.RepoDownloadsScreen.name) }
    val onSearchUsersClicked =
        { navHostController.navigate(NavigationScreens.SearchUserScreen.name) }

    CheckPermissions()

    MenuContent(
        onSearchReposClicked = onSearchClicked,
        onSearchUsersClicked = onSearchUsersClicked,
        onDownloadsClicked = onDownloadsClicked
    )
}


@Preview
@Preview()
@Composable
private fun PreviewMenuScreen(): Unit {
    MenuScreen(navHostController = rememberNavController())
}


@Composable
private fun CheckPermissions() {
    val context = LocalContext.current

    // Permissions to check
    val readStoragePermission = Manifest.permission.READ_EXTERNAL_STORAGE
    val writeStoragePermission = Manifest.permission.WRITE_EXTERNAL_STORAGE

    // Registering permission request launcher (for requesting permissions)
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // Permission granted, proceed with accessing the storage
            context.showLongToast("Permission is granted")
            restartApp(context)
        } else {
            // Handle the case where the permission is not granted
            context.showLongToast("Permission is not granted")
        }
    }

    LaunchedEffect(key1 = requestPermissionLauncher) {
        // Handle pre-Android 10 devices (legacy approach)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            context.requestPermission(readStoragePermission, requestPermissionLauncher)
            context.requestPermission(writeStoragePermission, requestPermissionLauncher)
        }
    }
}

private fun restartApp(context: Context) {
    val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
    val componentName = intent!!.component
    val mainIntent = Intent.makeRestartActivityTask(componentName)
    context.startActivity(mainIntent)
    Runtime.getRuntime().exit(0) // This will kill the app and restart it
}