package com.example.proyecto_gestion_de_recordatorios

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.rememberNavController
import com.example.proyecto_gestion_de_recordatorios.navegation.HomeScreen
import com.example.proyecto_gestion_de_recordatorios.navegation.NavegationWrapper
import com.example.proyecto_gestion_de_recordatorios.ui.theme.Proyecto_Gestion_de_RecordatoriosTheme
import com.google.firebase.auth.FirebaseAuth


class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        auth = FirebaseAuth.getInstance()

        setContent {
            Proyecto_Gestion_de_RecordatoriosTheme {
                val navControler = rememberNavController()
                NavegationWrapper(navControler, auth)
                LaunchedEffect(Unit) {
                    val currentUser = auth.currentUser
                    if (currentUser != null) {
                        navControler.navigate(HomeScreen) {
                            popUpTo(0)
                        }
                    }
                }
            }
        }
    }
}

