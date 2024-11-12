package com.example.composegitapp.ui.screen_menu

import android.Manifest
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
    val onDownloadsClicked = { navHostController.navigate(NavigationScreens.SearchRepoScreen.name) }
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
        } else {
            // Handle the case where the permission is not granted
            context.showLongToast("Permission is not granted")
        }
    }


    LaunchedEffect(key1 = Unit) {
        context.requestPermission(readStoragePermission, requestPermissionLauncher)
        context.requestPermission(writeStoragePermission, requestPermissionLauncher)
    }
}