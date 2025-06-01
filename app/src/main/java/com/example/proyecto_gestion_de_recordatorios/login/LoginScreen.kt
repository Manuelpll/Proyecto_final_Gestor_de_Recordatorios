package com.example.proyecto_gestion_de_recordatorios.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.proyecto_gestion_de_recordatorios.register.CustomButton
import com.example.proyecto_gestion_de_recordatorios.ui.theme.background_register_login_profile
import com.example.proyecto_gestion_de_recordatorios.ui.theme.button_login_newfriend
import com.example.proyecto_gestion_de_recordatorios.ui.theme.text_to_initial
import com.google.firebase.auth.FirebaseAuth

/**
 * Inicio de sesion de la Aplicacion
 */
@Composable
fun LoginScreen(
    navegateToHome: () -> Unit = {},
    navegateToRegister: () -> Unit = {},
    viewModel: LoginViewModel = hiltViewModel()
) {
    // Navegación automática si login fue exitoso
    if (viewModel.loginSuccess) {
        LaunchedEffect(Unit) {
            navegateToHome()
            viewModel.loginSuccess = false // Reseteamos para futuras veces
        }
    }

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(background_register_login_profile)
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(150.dp))

        Text(
            text = "Iniciar Sesión",
            fontSize = 40.sp,
            textDecoration = TextDecoration.Underline,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(40.dp))

        OutlinedTextField(
            value = viewModel.email,
            onValueChange = { viewModel.onEmailChange(it) },
            label = { Text("Correo electrónico") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = viewModel.password,
            onValueChange = { viewModel.onPasswordChange(it) },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = if (viewModel.passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val icon = if (viewModel.passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                val description = if (viewModel.passwordVisible) "Ocultar contraseña" else "Mostrar contraseña"
                IconButton(onClick = { viewModel.togglePasswordVisibility() }) {
                    Icon(imageVector = icon, contentDescription = description)
                }
            }
        )

        Spacer(modifier = Modifier.height(40.dp))

        CustomButton(
            text = "Iniciar sesión",
            backgroundColor = button_login_newfriend,
            onClick = { viewModel.onLoginClick() }
        )

        Spacer(modifier = Modifier.height(20.dp))

        TextButton(onClick = { navegateToRegister() }) {
            Text(
                text = "¿No tienes cuenta? Crear una",
                color = text_to_initial,
                fontSize = 16.sp,
                textDecoration = TextDecoration.Underline
            )
        }
    }

    if (viewModel.showErrorDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissErrorDialog() },
            title = { Text(text = "Error de inicio de sesión") },
            text = { Text(text = viewModel.errorMessage) },
            confirmButton = {
                Button(
                    onClick = { viewModel.dismissErrorDialog() }
                ) {
                    Text("Aceptar")
                }
            }
        )
    }
}