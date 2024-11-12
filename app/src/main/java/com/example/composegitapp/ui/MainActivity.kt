package com.example.composegitapp.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import com.example.composegitapp.App
import com.example.composegitapp.ui.theme.AppTheme
import javax.inject.Inject

class MainActivity : ComponentActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        (application as App).appComponent.inject(this)
//        val viewModelProvider = ViewModelProvider(viewModelStore, viewModelFactory)

        setContent {
            AppTheme {
                Surface(
                    modifier = Modifier
                        .statusBarsPadding()
                        .navigationBarsPadding(),
                ) {
                    NavHostCreator(viewModelFactory)
                }
            }
        }
    }

}

