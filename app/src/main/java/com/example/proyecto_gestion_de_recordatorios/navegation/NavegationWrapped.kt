package com.example.proyecto_gestion_de_recordatorios.navegation

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.proyecto_gestion_de_recordatorios.initial.InitialScreen
import com.example.proyecto_gestion_de_recordatorios.login.LoginScreen
import com.example.proyecto_gestion_de_recordatorios.register.RegisterScreen
@Preview
@Composable
fun NavegationWrapper() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = InitialScreen) {

        composable<InitialScreen> {
        InitialScreen(
                navegateToRegister={navController.navigate(RegisterScreen)},
            navegateToLogin= { navController.navigate(LoginScreen)}
            )
        }

        composable<RegisterScreen>{
         RegisterScreen(
             navegateToLogin= {navController.navigate(LoginScreen)}
         )
        }
        composable<LoginScreen>{
         LoginScreen(
             navegateToHome={navController.navigate(HomeScreen)},
             navegateToRegister={navController.navigate(RegisterScreen)}
         )
        }
    }
}