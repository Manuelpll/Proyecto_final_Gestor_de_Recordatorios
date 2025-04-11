package com.example.proyecto_gestion_de_recordatorios.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyecto_gestion_de_recordatorios.ui.theme.background_register_login_profile

@Preview
@Composable
fun RegisterScreen(){
    Column (verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
        .background(background_register_login_profile)) {
        Spacer(modifier = Modifier.height(150.dp))
        Text("Crear Cuenta",
            fontSize = 45.sp,
            textDecoration = TextDecoration.Underline)
        Spacer(modifier = Modifier.height(40.dp))


    }
}