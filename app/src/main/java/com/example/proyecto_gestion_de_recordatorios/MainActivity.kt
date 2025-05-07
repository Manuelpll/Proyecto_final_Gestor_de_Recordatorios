package com.example.proyecto_gestion_de_recordatorios

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.rememberNavController
import com.example.proyecto_gestion_de_recordatorios.navegation.HomeScreen
import com.example.proyecto_gestion_de_recordatorios.navegation.NavigationWrapper
import com.example.proyecto_gestion_de_recordatorios.ui.theme.Proyecto_Gestion_de_RecordatoriosTheme
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            Proyecto_Gestion_de_RecordatoriosTheme {
                val navController = rememberNavController()
                NavigationWrapper(navController)
                LaunchedEffect(Unit) {
                    val currentUser = auth.currentUser
                    if (currentUser != null) {
                        navController.navigate(HomeScreen) {
                            popUpTo(0)
                        }
                    }
                }
            }
        }
    }
}

