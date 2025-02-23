// app/src/main/java/com/watuke/watu/navigation/NavGraph.kt
package com.watuke.watu.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.watuke.watu.screens.LoginScreen
import com.watuke.watu.screens.MainScreen
import com.watuke.watu.screens.QrCodeScreen
import com.watuke.watu.screens.SplashScreen

@Composable
fun SetupNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        composable(route = "splash") {
            SplashScreen(navController = navController)
        }
        composable(route = "login") {
            LoginScreen(navController = navController)
        }
        composable(route = "MainScreen") {
            MainScreen(navController = navController)
        }
        composable(route = "QrCodeScreen") {
            QrCodeScreen(navController = navController)
        }
    }
}