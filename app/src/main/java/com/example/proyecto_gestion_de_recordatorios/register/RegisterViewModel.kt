package com.example.proyecto_gestion_de_recordatorios.register

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.proyecto_gestion_de_recordatorios.data.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * ViewModel de la RegisterScreen
 */
@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) : ViewModel() {

    var nombre by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var imageUri by mutableStateOf<Uri?>(null)
    var showErrorDialog by mutableStateOf(false)

    fun onNombreChange(newName: String) {
        nombre = newName
    }

    fun onEmailChange(newEmail: String) {
        email = newEmail
    }

    fun onPasswordChange(newPassword: String) {
        password = newPassword
    }

    fun onImageSelected(uri: Uri) {
        imageUri = uri
    }

    fun registerUser(onSuccess: () -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid ?: return@addOnCompleteListener

                    if (imageUri != null) {
                        val ref = storage.reference.child("profile_images/$userId.jpg")
                        ref.putFile(imageUri!!)
                            .continueWithTask { ref.downloadUrl }
                            .addOnSuccessListener { downloadUrl ->
                                saveUserInFirestore(userId, downloadUrl.toString(), onSuccess)
                            }
                    } else {
                        saveUserInFirestore(userId, null, onSuccess)
                    }
                } else {
                    showErrorDialog = true
                }
            }
    }

    private fun saveUserInFirestore(userId: String, imageUrl: String?, onSuccess: () -> Unit) {
        val usuario = Usuario(
            nombre = nombre,
            email = email,
            contactos = emptyList(),
            recordatorios_disponibles = emptyList()
        )

        val userMap = usuario.copy().toMap().toMutableMap()
        imageUrl?.let { userMap["FotosPerfil"] = it }

        firestore.collection("Users").document(userId).set(userMap)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { showErrorDialog = true }
    }
}

// Utilidad para convertir data class a Map
fun Usuario.toMap(): Map<String, Any?> = mapOf(
    "nombre" to nombre,
    "email" to email,
    "telefono" to telefono,
    "ubicacion" to ubicacion,
    "contactos" to contactos,
    "recordatorios_disponibles" to recordatorios_disponibles
)