package com.example.proyecto_gestion_de_recordatorios.profile

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) : ViewModel() {

    // Información del usuario
    private val _userName = MutableStateFlow("")
    val userName = _userName.asStateFlow()

    private val _userEmail = MutableStateFlow(auth.currentUser?.email ?: "")
    val userEmail = _userEmail.asStateFlow()

    private val _userId = MutableStateFlow(auth.currentUser?.uid ?: "")
    val userId = _userId.asStateFlow()

    private val _profileImageUrl = MutableStateFlow("")
    val profileImageUrl = _profileImageUrl.asStateFlow()

    // Variable para indicar que la sesión fue cerrada
    private val _logoutSuccess = MutableStateFlow(false)
    val logoutSuccess = _logoutSuccess.asStateFlow()

    init {
        loadUserData()
        loadProfileImage()
    }

    fun logout() {
        auth.signOut()
        _logoutSuccess.value = true
    }

    private fun loadUserData() {
        val uid = auth.currentUser?.uid ?: return

        firestore.collection("usuarios").document(uid).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    _userName.value = document.getString("nombre") ?: ""
                    _userEmail.value = document.getString("correo") ?: auth.currentUser?.email.orEmpty()
                    _userId.value = uid
                }
            }
            .addOnFailureListener {
                // Puedes agregar logs o control de errores si quieres
            }
    }

    // Método para editar datos (telefono, ubicación, correo)
    fun editUserData(
        newTelefono: String,
        newUbicacion: String,
        newCorreo: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        val user = auth.currentUser
        val uid = user?.uid ?: return

        val userRef = firestore.collection("Users").document(uid)

        // Primero actualizamos Firestore
        userRef.get().addOnSuccessListener { document ->
            val updates = mutableMapOf<String, Any>()
            updates["telefono"] = newTelefono
            updates["ubicacion"] = newUbicacion
            updates["correo"] = newCorreo

            userRef.update(updates)
                .addOnSuccessListener {
                    // Ahora actualizamos el correo en Auth
                    user.updateEmail(newCorreo)
                        .addOnSuccessListener {
                            _userEmail.value = newCorreo
                            onSuccess()
                        }
                        .addOnFailureListener { authError ->
                            onFailure("Error al actualizar correo en autenticación: ${authError.message}")
                        }
                }
                .addOnFailureListener { firestoreError ->
                    onFailure("Error al actualizar datos en Firestore: ${firestoreError.message}")
                }
        }.addOnFailureListener { error ->
            onFailure("Error al obtener datos: ${error.message}")
        }
    }

    // Método para recuperar la imagen de perfil desde Storage
    fun loadProfileImage() {
        val uid = auth.currentUser?.uid ?: return

        val imageRef = storage.reference.child("profile_images/$uid.jpg")
        imageRef.downloadUrl
            .addOnSuccessListener { uri ->
                _profileImageUrl.value = uri.toString()
            }
            .addOnFailureListener {
                _profileImageUrl.value = ""
            }
    }
}