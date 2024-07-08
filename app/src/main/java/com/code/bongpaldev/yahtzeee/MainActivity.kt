package com.code.bongpaldev.yahtzeee

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.code.bongpaldev.yahtzeee.ui.game.GameScreen
import com.code.bongpaldev.yahtzeee.ui.result.ResultScreen
import com.code.bongpaldev.yahtzeee.ui.theme.YahtzeeeTheme

const val HOME_SCREEN = "home"
const val GAME_SCREEN = "game"
const val RESULT_SCREEN = "result"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            YahtzeeeTheme {

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = GAME_SCREEN) {
                        composable(GAME_SCREEN,
                            enterTransition = {
                                fadeIn(
                                    animationSpec = tween(1000)
                                )
                            },
                            exitTransition = { fadeOut(animationSpec = tween(1000)) }) {
                            GameScreen(
                                navController,
                                innerPadding
                            )
                        }
                        composable(
                            RESULT_SCREEN,
                            enterTransition = {
                                fadeIn(
                                    animationSpec = tween(1000)
                                )
                            },
                            exitTransition = { fadeOut(animationSpec = tween(1000)) }
                        ) { ResultScreen(navController, innerPadding) }
                    }
                }
            }
        }
    }
}