package com.example.proyecto_gestion_de_recordatorios.otherScreens.category

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.compose.ui.graphics.Color

/**
 * ViewModel de la CategoryScreen
 */
@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage,
    private val auth: FirebaseAuth
) : ViewModel() {

    var categorias by mutableStateOf<List<Pair<String, Color>>>(emptyList())
        private set

    var fotoPerfilUrl by mutableStateOf<String?>(null)
        private set

    fun cargarCategorias() {
        val uid = auth.currentUser?.uid
        if (uid != null) {
            firestore.collection("Categories")
                .whereEqualTo("usuarioId", uid)
                .get()
                .addOnSuccessListener { result ->
                    val categoriasTemp = result.documents.mapNotNull { doc ->
                        val nombre = doc.getString("nombre")
                        val colorHex = doc.getString("color")
                        if (nombre != null && colorHex != null) {
                            val color = try {
                                Color(android.graphics.Color.parseColor("#$colorHex"))
                            } catch (e: Exception) {
                                null
                            }
                            if (color != null) nombre to color else null
                        } else null
                    }
                    categorias = categoriasTemp
                }
        }
    }

    fun cargarFotoPerfil() {
        val uid = auth.currentUser?.uid ?: return

        val storageRef = storage.reference.child("profile_images/$uid.jpg")
        storageRef.downloadUrl
            .addOnSuccessListener { uri ->
                fotoPerfilUrl = uri.toString()
            }
    }
}