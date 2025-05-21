package com.example.proyecto_gestion_de_recordatorios.home

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.proyecto_gestion_de_recordatorios.data.Recordatorio
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import com.google.firebase.storage.FirebaseStorage
import androidx.compose.runtime.State
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) : ViewModel() {

    private val _recordatorios = mutableStateListOf<Recordatorio>()
    val recordatorios: List<Recordatorio> get() = _recordatorios

    private val _profileImageUrl = mutableStateOf<String?>(null)
    val profileImageUrl: State<String?> get() = _profileImageUrl

    init {
        obtenerTodosLosRecordatorios()
        obtenerFotoPerfil()
    }

    private fun obtenerTodosLosRecordatorios() {
        val userId = auth.currentUser?.uid ?: return
        val userPath = "/Users/$userId"

        firestore.collectionGroup("Reminders")
            .get()
            .addOnSuccessListener { querySnapshot ->
                val recordatoriosList = querySnapshot.documents.mapNotNull { doc ->
                    try {
                        val id = doc.id
                        val titulo = doc.getString("titulo")
                        val fecha = doc.getString("fecha_hora")
                        val descripcion = doc.getString("descripcion")
                        val colorHex = doc.getString("color") ?: "#FFFFFF"
                        val color = Color(android.graphics.Color.parseColor(colorHex))
                        val colorCategoriaHex = doc.getString("color_categoria") ?: "#FFFFFF"
                        val colorCategoria = Color(android.graphics.Color.parseColor(colorCategoriaHex))
                        val esFavorito = doc.getBoolean("favorito") ?: false
                        val listaCompartidos = (doc.get("compartido_con") as? List<String>) ?: emptyList()
                        val estaCompartido = listaCompartidos.isNotEmpty()
                        val compartidoPor = doc.getString("creador") ?: ""

                        Recordatorio(
                            id =id,
                            titulo = titulo,
                            fecha = fecha,
                            descripcion = descripcion,
                            color = color.toString(),
                            color_de_la_categoria = colorCategoria.toString(),
                            esFavorito = esFavorito,
                            esta_Compartido = estaCompartido,
                            lista_compartidos = listaCompartidos,
                            compartidoPor = compartidoPor
                        )
                    } catch (e: Exception) {
                        null
                    }
                }
                _recordatorios.clear()
                _recordatorios.addAll(recordatoriosList)
            }
    }

    private fun obtenerFotoPerfil() {
        val userId = auth.currentUser?.uid ?: return
        val storageRef = storage.reference.child("profile_images/$userId.jpg")

        storageRef.downloadUrl
            .addOnSuccessListener { uri ->
                _profileImageUrl.value = uri.toString()
            }
            .addOnFailureListener {
                // Si falla, podemos dejar null y mostrar el icono por defecto
                _profileImageUrl.value = null
            }
    }
}