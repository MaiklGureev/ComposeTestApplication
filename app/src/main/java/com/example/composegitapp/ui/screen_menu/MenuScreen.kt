package com.example.composegitapp.ui.screen_menu

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.example.composegitapp.common.extension.requestPermission
import com.example.composegitapp.common.extension.showLongToast
import com.example.composegitapp.ui.NavigationScreens
import com.example.composegitapp.ui.screen_menu.widgets.GitHubTokenDialog
import com.example.composegitapp.ui.screen_menu.widgets.MenuContent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun MenuScreen(
    mainMenuViewModel: MainMenuViewModel,
    navHostController: NavHostController,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val isTokenFilled by rememberSaveable { mutableStateOf(mainMenuViewModel.isTokenFilled()) }

    val onSearchClicked = { navHostController.navigate(NavigationScreens.SearchRepoScreen.name) }
    val onDownloadsClicked =
        { navHostController.navigate(NavigationScreens.RepoDownloadsScreen.name) }
    val onSearchUsersClicked =
        { navHostController.navigate(NavigationScreens.SearchUserScreen.name) }

    val onResetTokenClicked: () -> Unit = {
        scope.launch {
            mainMenuViewModel.resetToken()
            delay(500)
            restartApp(context)
        }
    }


    CheckPermissions()
    GitHubTokenDialog(
        isTokenFilled = isTokenFilled,
        onTokenEntered = { value -> mainMenuViewModel.saveToken(value) },
        onExitApp = { exitApp() },
        onRestartApp = { restartApp(context = context) }
    )

    MenuContent(
        onSearchReposClicked = onSearchClicked,
        onSearchUsersClicked = onSearchUsersClicked,
        onDownloadsClicked = onDownloadsClicked,
        onResetTokenClicked = onResetTokenClicked,
    )
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
    val componentName = intent?.component
    val mainIntent = Intent.makeRestartActivityTask(componentName)
    context.startActivity(mainIntent)
    Runtime.getRuntime().exit(0) // This will kill the app and restart it
}

private fun exitApp() {
    Runtime.getRuntime().exit(0)
}