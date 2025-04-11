package com.example.proyecto_gestion_de_recordatorios.initial

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyecto_gestion_de_recordatorios.R
import com.example.proyecto_gestion_de_recordatorios.ui.theme.button_initial
import com.example.proyecto_gestion_de_recordatorios.ui.theme.initial_28
import com.example.proyecto_gestion_de_recordatorios.ui.theme.initial_61
import com.example.proyecto_gestion_de_recordatorios.ui.theme.text_to_initial

@Preview
@Composable
fun InitialScreen() {
    val colorsGradient = arrayOf(
        0.0f to initial_61,
        1.0f to initial_28
    )

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(colorStops = colorsGradient))
    ) {
        Spacer(modifier = Modifier.height(150.dp))
        Image( painter = painterResource(id = R.drawable.gestion_de_recordatorios_bordes_redondeados) , contentDescription = "Icono aplicacion", modifier = Modifier.size(200.dp))
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Gestor de recordatorios",
            fontSize = 29.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth() // Asegúrate de que tenga espacio para centrarse
        )
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = {},
            modifier = Modifier
                .height(60.dp)
                .width(220.dp),
            shape = RoundedCornerShape(40.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = button_initial,
                contentColor = Color.Black
            )
        ) {
            Text("Iniciar sesión", fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(40.dp))
        TextButton(onClick = {}) {
            Text(
                text = "Crear cuenta",
                color = text_to_initial,
                textDecoration = TextDecoration.Underline,
                fontSize = 19.sp
            )
        }
    }
}