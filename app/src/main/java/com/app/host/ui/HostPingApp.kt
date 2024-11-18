package com.app.host.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.app.host.ui.feature.home.homeScreen

/**
 * Function is responsible for handling all the screens and navigation between screens
 */
@Composable
fun HostPingApp() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.Home.routeName) {
        homeScreen()
    }
}
