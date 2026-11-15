package com.hoanghai.app209.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

object Routes {
    const val HOME = "home"
    const val GAMES = "games"
    const val SETTINGS = "settings"
}

@Composable
fun NavGraph() {
    val nav = rememberNavController()
    NavHost(navController = nav, startDestination = Routes.HOME) {
        composable(Routes.HOME) { HomeScreen(nav) }
        composable(Routes.GAMES) { GameListScreen(nav) }
        composable(Routes.SETTINGS) { SettingsScreen(nav) }
    }
}
