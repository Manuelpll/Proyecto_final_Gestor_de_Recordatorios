package com.example.proyecto_gestion_de_recordatorios.login
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * ViewModel del LoginScreen
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val auth: FirebaseAuth
) : ViewModel() {

    var email by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    var passwordVisible by mutableStateOf(false)
        private set

    var showErrorDialog by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf("")
        private set

    var loginSuccess by mutableStateOf(false)

    fun onEmailChange(newEmail: String) {
        email = newEmail
    }

    fun onPasswordChange(newPassword: String) {
        password = newPassword
    }

    fun togglePasswordVisibility() {
        passwordVisible = !passwordVisible
    }

    fun onLoginClick() {
        if (email.isBlank() || password.isBlank()) {
            errorMessage = "Por favor, introduce un correo y una contraseña."
            showErrorDialog = true
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    loginSuccess = true
                } else {
                    val firebaseError = task.exception?.message ?: "Error desconocido"
                    errorMessage = when {
                        firebaseError.contains("password is invalid", ignoreCase = true) -> "La contraseña no es válida."
                        firebaseError.contains("no user record", ignoreCase = true) -> "No existe una cuenta con este correo."
                        else -> "Error al iniciar sesión. Intenta de nuevo."
                    }
                    showErrorDialog = true
                }
            }
    }

    fun dismissErrorDialog() {
        showErrorDialog = false
    }
}