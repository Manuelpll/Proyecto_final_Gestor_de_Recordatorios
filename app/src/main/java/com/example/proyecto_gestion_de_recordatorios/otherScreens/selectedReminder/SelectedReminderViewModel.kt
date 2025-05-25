package com.example.proyecto_gestion_de_recordatorios.otherScreens.selectedReminder

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto_gestion_de_recordatorios.data.Recordatorio
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
@HiltViewModel
class SelectedReminderViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) : ViewModel() {

    private val _selectedReminder = MutableStateFlow<Recordatorio?>(null)
    val selectedReminder: StateFlow<Recordatorio?> = _selectedReminder

    private val _profilePhotoUrl = MutableStateFlow<String?>(null)
    val profilePhotoUrl: StateFlow<String?> = _profilePhotoUrl

    fun loadReminderById(id: String) {
        viewModelScope.launch {
            try {
                val uid = auth.currentUser?.uid
                if (uid == null) {
                    Log.e("SelectedReminderVM", "Usuario no autenticado")
                    return@launch
                }

                Log.d("SelectedReminderVM", "Buscando recordatorio con ID: $id para user $uid")

                val docSnapshot = firestore.collection("Users")
                    .document(uid)
                    .collection("Reminders")
                    .document(id)
                    .get().await()

                if (docSnapshot.exists()) {
                    val reminder = docSnapshot.toObject(Recordatorio::class.java)
                    _selectedReminder.value = reminder
                    Log.d("SelectedReminderVM", "Recordatorio encontrado: $reminder")
                } else {
                    Log.w("SelectedReminderVM", "No se encontró el recordatorio con ID: $id")
                    _selectedReminder.value = null
                }
            } catch (e: Exception) {
                Log.e("SelectedReminderVM", "Error al obtener recordatorio: ${e.message}")
                _selectedReminder.value = null
            }
        }
    }

    fun loadProfilePhoto() {
        viewModelScope.launch {
            try {
                val storageRef = storage.reference.child("profile_images/${auth.currentUser?.uid}.jpg")
                val url = storageRef.downloadUrl.await()
                _profilePhotoUrl.value = url.toString()
            } catch (e: Exception) {
                _profilePhotoUrl.value = null
            }
        }
    }
}