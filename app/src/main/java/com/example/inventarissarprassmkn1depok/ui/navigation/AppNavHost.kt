package com.example.inventarissarprassmkn1depok.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.inventarissarprassmkn1depok.data.local.AppDatabase
import com.example.inventarissarprassmkn1depok.data.repository.AuthRepository
import com.example.inventarissarprassmkn1depok.ui.screens.auth.AuthViewModel
import com.example.inventarissarprassmkn1depok.ui.screens.auth.LoginScreen
import com.example.inventarissarprassmkn1depok.ui.screens.main.MainScaffoldScreen
import com.example.inventarissarprassmkn1depok.ui.screens.splash.SplashScreen

@Composable
fun AppNavHost(
    database: AppDatabase,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    val authRepository = remember { AuthRepository(database.adminDao()) }
    val authViewModel: AuthViewModel = viewModel(
        factory = AuthViewModel.Factory(authRepository)
    )

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
        modifier = modifier
    ) {
        // Screen 1: Splash Screen
        composable(Screen.Splash.route) {
            SplashScreen(
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        // Screen 2: Login Screen
        composable(Screen.Login.route) {
            LoginScreen(
                viewModel = authViewModel,
                onLoginSuccess = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        // Main App Scaffold (Dashboard Operasional, Katalog Sarpras, Sirkulasi, Laporan)
        composable(Screen.Dashboard.route) {
            MainScaffoldScreen(
                database = database,
                onLogout = {
                    authRepository.logout()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Dashboard.route) { inclusive = true }
                    }
                }
            )
        }
    }
}
