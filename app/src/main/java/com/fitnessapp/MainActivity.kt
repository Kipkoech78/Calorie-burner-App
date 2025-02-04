package com.fitnessapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.fitnessapp.domain.useCases.AppEntryUseCases
import com.fitnessapp.presentation.credentials.AddCredentialsScreen
import com.fitnessapp.presentation.credentials.SaveCredentialsViewModel
import com.fitnessapp.ui.theme.FitnessAppTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint //very necessary annotation for Hilt injection
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var appEntryUseCases: AppEntryUseCases
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        lifecycleScope.launch {
            appEntryUseCases.readAppEntry().collect{
                Log.d("Entry USe case: ", it.toString())
            }

        }
        enableEdgeToEdge()
        setContent {
            FitnessAppTheme {
                val isSystemInDarkMode = isSystemInDarkTheme()
                val systemController = rememberSystemUiController()
                SideEffect {
                    systemController.setSystemBarsColor(
                        color = Color.Transparent,
                        darkIcons = !isSystemInDarkMode
                    )
                }
                Box(modifier = Modifier.background(color = MaterialTheme.colorScheme.background)){
                    val viewModel : SaveCredentialsViewModel = hiltViewModel()
                    AddCredentialsScreen(event = viewModel::onEvent)
                }
            }
        }
    }
}
