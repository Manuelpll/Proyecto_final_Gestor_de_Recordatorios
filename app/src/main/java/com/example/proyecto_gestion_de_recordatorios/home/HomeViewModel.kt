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
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot

/**
 * ViewModel de HomeScreen
 */
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

        val userDocRef = firestore.collection("Users").document(userId)

        userDocRef.get().addOnSuccessListener { userSnapshot ->
            val recordatoriosCompartidosRefs = userSnapshot.get("recordatorios_disponibles") as? List<DocumentReference> ?: emptyList()

            firestore.collection("Users").document(userId).collection("Reminders")
                .get()
                .addOnSuccessListener { propiosSnapshot ->

                    val propiosList = propiosSnapshot.documents.mapNotNull { doc ->
                        docToRecordatorio(doc)
                    }.toMutableList()

                    val recordatoriosIds = propiosList.map { it.id }.toMutableSet()


                    if (recordatoriosCompartidosRefs.isNotEmpty()) {
                        val tareas = recordatoriosCompartidosRefs.map { ref ->
                            ref.get()
                        }

                        Tasks.whenAllSuccess<DocumentSnapshot>(tareas)
                            .addOnSuccessListener { documentosCompartidos ->
                                documentosCompartidos.forEach { doc ->
                                    val recordatorio = docToRecordatorio(doc)
                                    recordatorio?.let {
                                        if (!recordatoriosIds.contains(it.id)) {
                                            propiosList.add(it)
                                            recordatoriosIds.add(it.id)
                                        }
                                    }
                                }


                                _recordatorios.clear()
                                _recordatorios.addAll(propiosList)
                            }
                    } else {
                        _recordatorios.clear()
                        _recordatorios.addAll(propiosList)
                    }
                }
        }
    }

    private fun docToRecordatorio(doc: DocumentSnapshot): Recordatorio? {
        return try {
            val id = doc.id
            val titulo = doc.getString("titulo")
            val fecha = doc.getString("fecha_hora")
            val descripcion = doc.getString("descripcion")
            val colorHex = doc.getString("color") ?: "#FFFFFF"
            val colorCategoriaHex = doc.getString("color_de_la_categoria") ?: "#FFFFFF"
            val esFavorito = doc.getBoolean("favorito") ?: false
            val listaCompartidos = (doc.get("compartido_con") as? List<String>) ?: emptyList()
            val estaCompartido = listaCompartidos.isNotEmpty()
            val compartidoPor = doc.getString("creador") ?: ""

            Recordatorio(
                id = id,
                titulo = titulo,
                fecha = fecha,
                descripcion = descripcion,
                color = colorHex,
                color_de_la_categoria = colorCategoriaHex,
                esFavorito = esFavorito,
                esta_Compartido = estaCompartido,
                lista_compartidos = listaCompartidos,
                compartidoPor = compartidoPor
            )
        } catch (e: Exception) {
            null
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
                _profileImageUrl.value = null
            }
    }
}