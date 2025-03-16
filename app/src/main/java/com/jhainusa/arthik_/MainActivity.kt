package com.jhainusa.arthik_

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.jhainusa.arthik_.BackendFiles.AppDatabase


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val db = AppDatabase.getDatabase(application) // Modify as needed
            AppWithNavigation(db)
        }
    }
}
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppWithNavigation(
    db: AppDatabase) {

    val navController = rememberNavController()
    AnimatedNavHost(
        navController = navController,
        startDestination = "screen1"
    ) {
        composable("screen1",
            exitTransition = { slideOutVertically(targetOffsetY = { -it },
                animationSpec = tween(500)
            ) }
        ) { screen(navController) }
        composable("screen3",
            enterTransition = {
                slideInVertically(initialOffsetY = { it })
            },
                    exitTransition = {
                slideOutHorizontally(targetOffsetX = { -it })
            }){ lang(navController) }
        composable("screen2",

            enterTransition = {
                slideInHorizontally(initialOffsetX = { it })
            },
            exitTransition = {
                slideOutHorizontally(targetOffsetX = { -it })
            }
            ) { loginScreen(navController) }

        composable("screen4",

            enterTransition = {
                slideInHorizontally(initialOffsetX = { it })
            },
            exitTransition = {
                slideOutHorizontally(targetOffsetX = { -it })
            }){
            BottomNavBarApp(db,navController) }

        composable(
            route = "screen5/{type}/{itemname}",
            arguments = listOf(navArgument("type") { type = NavType.StringType },
                navArgument("itemname"){type = NavType.StringType}
            )
        ) { backStackEntry ->
            val type = backStackEntry.arguments?.getString("type")
            val itemname = backStackEntry.arguments?.getString("itemname")
            startCapture(type.toString(),itemname.toString())
        }
    }
}
