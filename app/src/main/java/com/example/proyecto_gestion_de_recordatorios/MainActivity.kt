package com.example.proyecto_gestion_de_recordatorios

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.NavHostController
import com.example.proyecto_gestion_de_recordatorios.navegation.HomeScreen
import com.example.proyecto_gestion_de_recordatorios.navegation.NavegationWrapper
import com.example.proyecto_gestion_de_recordatorios.ui.theme.Proyecto_Gestion_de_RecordatoriosTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class MainActivity : ComponentActivity() {
private lateinit var navControler:NavHostController
    private lateinit var auth :FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        auth= Firebase.auth
        setContent {
            Proyecto_Gestion_de_RecordatoriosTheme {
                NavegationWrapper(navControler,auth)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser= auth.currentUser
        if (currentUser!=null){
            navControler.navigate(HomeScreen)
        }
    }
}

