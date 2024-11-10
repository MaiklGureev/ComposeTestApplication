package com.example.composegitapp.ui.screen_splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.example.composegitapp.ui.NavigationScreens
import com.gureev.ComposeAppGitHub.R
import kotlinx.coroutines.delay

@Composable

fun SplashScreen(
    navHostController: NavHostController,
    delay: Long
) {
    SplashContent()
    LaunchedEffect(true) {
        delay(delay)
        navHostController.navigate(NavigationScreens.Menu.name) {
            popUpTo(NavigationScreens.Splash.name) { inclusive = true }
        }
    }
}

@Preview()
@Composable
fun SplashContent() {
    val image: Painter = painterResource(id = R.drawable.ic_splash_logo)
    Image(
        painter = image,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentDescription = null
    )
}

