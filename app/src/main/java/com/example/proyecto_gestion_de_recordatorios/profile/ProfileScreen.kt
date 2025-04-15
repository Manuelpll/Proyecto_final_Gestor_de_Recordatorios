package com.example.proyecto_gestion_de_recordatorios.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyecto_gestion_de_recordatorios.ui.theme.background_register_login_profile
import com.example.proyecto_gestion_de_recordatorios.ui.theme.bar
import com.example.proyecto_gestion_de_recordatorios.ui.theme.button_profile_cerrar_sesion

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun ProfileScreen() {
    Scaffold(
        bottomBar = {
            BottomAppBar(
                containerColor = bar,
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                IconButton(onClick = { /* Acción de volver */ }) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver",
                        tint = Color.Black
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(background_register_login_profile),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(120.dp))

            Icon(
                Icons.Default.AccountCircle,
                contentDescription = "Foto de perfil",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Nombre
            Text("Manuel Parra", fontSize = 22.sp, fontWeight = FontWeight.Bold)

            // ID personal (no editable)
            Text(
                text = "Id personal: 14782982112nj32r",
                fontSize = 14.sp,
                modifier = Modifier
                    .padding(top = 4.dp)
                    .background(Color.White, RoundedCornerShape(8.dp))
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Tarjeta de información personal
            Card(
                modifier = Modifier.fillMaxWidth(0.8f),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Información personal", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Correo electrónico: prueba@gmail.com")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Teléfono: 6892345601")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Ubicación: Alcorcón, Madrid")
                }

                // Icono de edición
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    IconButton(onClick = { /* Abrir modo edición */ }) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "Editar datos",
                            tint = Color.Gray
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Botón Cerrar sesión
            Button(
                onClick = { /* Lógica cerrar sesión */ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = button_profile_cerrar_sesion,
                    contentColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                Text("Cerrar Sesión")
            }

            Spacer(modifier = Modifier.weight(1f))

        }
    }
}
