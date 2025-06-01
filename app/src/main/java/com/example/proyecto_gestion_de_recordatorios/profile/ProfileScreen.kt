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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.proyecto_gestion_de_recordatorios.ui.theme.background_register_login_profile
import com.example.proyecto_gestion_de_recordatorios.ui.theme.bar
import com.example.proyecto_gestion_de_recordatorios.ui.theme.button_profile_cerrar_sesion

/**
 * Pantalla del perfil de la aplicación
 */
@Composable
fun ProfileScreen(
    navigateToInitial: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel(),
    navigateToBack: () -> Boolean
) {
    val userName by viewModel.userName.collectAsState()
    val userEmail by viewModel.userEmail.collectAsState()
    val userId by viewModel.userId.collectAsState()
    val userPhone by viewModel.userPhone.collectAsState()
    val userUbication by viewModel.userUbication.collectAsState()
    val profileImageUrl by viewModel.profileImageUrl.collectAsState()
    val logoutSuccess by viewModel.logoutSuccess.collectAsState()

    // Campos para edición
    var showEditDialog by remember { mutableStateOf(false) }
    var newTelefono by remember { mutableStateOf("") }
    var newUbicacion by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    LaunchedEffect(logoutSuccess) {
        if (logoutSuccess) {
            navigateToInitial()
        }
    }

    Scaffold(
        bottomBar = {
            BottomAppBar(
                containerColor = bar,
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                IconButton(onClick = { navigateToBack() }) {
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
            Spacer(modifier = Modifier.height(100.dp))

            // Imagen perfil
            if (profileImageUrl.isNotEmpty()) {
                AsyncImage(
                    model = profileImageUrl,
                    contentDescription = "Foto de perfil",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray),
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(
                    Icons.Default.AccountCircle,
                    contentDescription = "Foto de perfil",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(userName, fontSize = 22.sp, fontWeight = FontWeight.Bold)

            // ID personal
            Text(
                text = "Id personal: $userId",
                fontSize = 14.sp,
                modifier = Modifier
                    .padding(top = 4.dp)
                    .background(Color.White, RoundedCornerShape(8.dp))
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Tarjeta con datos
            Card(
                modifier = Modifier.fillMaxWidth(0.8f),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Información personal", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Correo electrónico: $userEmail")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Teléfono: $userPhone")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Ubicación: $userUbication")
                }

                // Icono de edición
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    IconButton(onClick = { showEditDialog = true }) {
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
                onClick = { viewModel.logout() },
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

    // Modo edición
    if (showEditDialog) {
        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = { Text("Editar datos") },
            text = {
                Column {
                    OutlinedTextField(
                        value = newTelefono,
                        onValueChange = { newTelefono = it },
                        label = { Text("Nuevo teléfono") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = newUbicacion,
                        onValueChange = { newUbicacion = it },
                        label = { Text("Nueva ubicación") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    val telefonoRegex = Regex("^[0-9]{9}\$")

                    if (!telefonoRegex.matches(newTelefono)) {
                        errorMessage = "El teléfono debe tener  9 dígitos "
                        showError = true
                    } else {
                        viewModel.editUserData(
                            newTelefono,
                            newUbicacion,
                            onSuccess = {
                                showEditDialog = false
                                newTelefono = ""
                                newUbicacion = ""
                            },
                            onFailure = { error ->
                                errorMessage = error
                                showError = true
                            }
                        )
                    }
                }) {
                    Text("Guardar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    // Dialogo error
    if (showError) {
        AlertDialog(
            onDismissRequest = { showError = false },
            title = { Text("Error") },
            text = { Text(errorMessage) },
            confirmButton = {
                Button(onClick = { showError = false }) {
                    Text("Aceptar")
                }
            }
        )
    }
}
