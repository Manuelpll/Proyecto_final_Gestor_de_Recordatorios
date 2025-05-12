package com.example.proyecto_gestion_de_recordatorios.otherScreens.selectedReminder

import android.net.Uri
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
                val docSnapshot = firestore.collection("Users")
                    .document(auth.currentUser?.uid ?: "")
                    .collection("Reminders")
                    .document(id)
                    .get().await()
                val reminder = docSnapshot.toObject(Recordatorio::class.java)
                _selectedReminder.value = reminder
            } catch (e: Exception) {
                _selectedReminder.value = null
            }
        }
    }

    fun loadProfilePhoto() {
        viewModelScope.launch {
            try {
                val storageRef = storage.reference.child("profile_photos/${auth.currentUser?.uid}.jpg")
                val url = storageRef.downloadUrl.await()
                _profilePhotoUrl.value = url.toString()
            } catch (e: Exception) {
                _profilePhotoUrl.value = null
            }
        }
    }
}