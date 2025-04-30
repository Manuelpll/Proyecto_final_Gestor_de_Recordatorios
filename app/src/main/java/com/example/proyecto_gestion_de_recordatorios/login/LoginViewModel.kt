package com.example.proyecto_gestion_de_recordatorios.login
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class LoginViewModel(private val auth: FirebaseAuth) : ViewModel()  {
    var email = mutableStateOf("")
    var password = mutableStateOf("")
    var passwordVisible = mutableStateOf(false)
    var showErrorDialog = mutableStateOf(false)
    var errorMessage = mutableStateOf("")
    var loginSuccess = mutableStateOf(false)

    fun onLoginClick() {
        auth.signInWithEmailAndPassword(email.value, password.value)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    loginSuccess.value = true
                } else {
                    val firebaseError = task.exception?.message ?: "Error desconocido"
                    errorMessage.value = when {
                        firebaseError.contains("password is invalid", ignoreCase = true) -> "La contraseña no es válida."
                        firebaseError.contains("no user record", ignoreCase = true) -> "No existe una cuenta con este correo."
                        else -> "Error al iniciar sesión. Intenta de nuevo."
                    }
                    showErrorDialog.value = true
                }
            }
    }
}