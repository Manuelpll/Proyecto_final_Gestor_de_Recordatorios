package com.example.proyecto_gestion_de_recordatorios.profile

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProfileViewModel(private val auth: FirebaseAuth): ViewModel() {
    //Informacíon usuarío
    private val _userEmail = MutableStateFlow(auth.currentUser?.email ?: "")
    val userEmail = _userEmail.asStateFlow()

    private val _userId = MutableStateFlow(auth.currentUser?.uid ?: "")
    val userId = _userId.asStateFlow()
    //Variable para inicdicar que la sesíon fue cerrada
    private val _logoutSuccess = MutableStateFlow(false)
    val logoutSuccess = _logoutSuccess.asStateFlow()

    fun logout() {
        auth.signOut()
        _logoutSuccess.value = true
    }
}