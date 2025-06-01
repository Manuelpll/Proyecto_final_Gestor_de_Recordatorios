package com.example.proyecto_gestion_de_recordatorios.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

/**
 * ViewModel de la ProfileScreen
 */
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) : ViewModel() {

    // Información del usuario
    private val _userName = MutableStateFlow("")
    val userName = _userName.asStateFlow()

    private val _userPhone = MutableStateFlow("")
    val userPhone = _userPhone.asStateFlow()

    private val _userEmail = MutableStateFlow(auth.currentUser?.email ?: "")
    val userEmail = _userEmail.asStateFlow()

    private val _userId = MutableStateFlow(auth.currentUser?.uid ?: "")
    val userId = _userId.asStateFlow()

    private val _userUbication = MutableStateFlow("")
    val userUbication = _userUbication.asStateFlow()

    private val _profileImageUrl = MutableStateFlow("")
    val profileImageUrl = _profileImageUrl.asStateFlow()


    private val _logoutSuccess = MutableStateFlow(false)
    val logoutSuccess = _logoutSuccess.asStateFlow()

    init {
        loadUserData()
        loadProfileImage()
    }
    // Método para cerrar sesion
    fun logout() {
        auth.signOut()
        _logoutSuccess.value = true
    }
    // Método para cargar la informacion del usuario
    private fun loadUserData() {
        val uid = auth.currentUser?.uid ?: return

        firestore.collection("Users").document(uid).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    _userName.value = document.getString("nombre") ?: ""
                    _userEmail.value = document.getString("email") ?: auth.currentUser?.email.orEmpty()
                    _userId.value = uid
                    _userUbication.value=document.getString("ubicacion") ?:""
                    _userPhone.value=document.getString("telefono") ?:""
                }
            }
            .addOnFailureListener {
                Log.e("Error de carga","No se ha podido cargar la información")
            }
    }

    // Método para editar datos (telefono, ubicación)
    fun editUserData(
        newTelefono: String,
        newUbicacion: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        val user = auth.currentUser
        val uid = user?.uid ?: return

        val userRef = firestore.collection("Users").document(uid)


        userRef.get().addOnSuccessListener { document ->
            val updates = mutableMapOf<String, Any>()
            updates["telefono"] = newTelefono
            updates["ubicacion"] = newUbicacion

            userRef.update(updates)
                .addOnSuccessListener {
                    loadUserData()
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