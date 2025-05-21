package com.example.proyecto_gestion_de_recordatorios.otherScreens.reminder

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import com.example.proyecto_gestion_de_recordatorios.data.Recordatorio
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FieldPath
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.compose.ui.graphics.Color
@HiltViewModel
class ReminderViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) : ViewModel() {

    private val _recordatorios = mutableStateListOf<Recordatorio>()
    val recordatorios: List<Recordatorio> get() = _recordatorios

    private val _searchQuery = mutableStateOf("")
    val searchQuery: State<String> get() = _searchQuery

    private val _fotoPerfilUrl= mutableStateOf<String?>(null)
    val fotoPerfilUrl: State<String?> get() = _fotoPerfilUrl

    init {
        cargarRecordatorios()
        cargarImagenPerfil()
    }

    fun cargarImagenPerfil() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val ref = storage.reference.child("profile_images/$userId.jpg")
            ref.downloadUrl.addOnSuccessListener { uri ->
                _fotoPerfilUrl.value = uri.toString()
            }
        }
    }

    fun cargarRecordatorios() {
        val uid = auth.currentUser?.uid
        if (uid != null) {
            firestore.collection("Users").document(uid)
                .get()
                .addOnSuccessListener { userSnapshot ->
                    val todosIds =  userSnapshot.get("recordatorios_disponibles") as? List<String> ?: emptyList()
                    if (todosIds.isNotEmpty()) {
                        firestore.collectionGroup("Reminders")
                            .whereIn(FieldPath.documentId(), todosIds)
                            .get()
                            .addOnSuccessListener { snapshot ->
                                val lista = snapshot.documents.mapNotNull { doc ->
                                    val titulo = doc.getString("titulo")
                                    val fecha = doc.getString("fecha_hora")
                                    val descripcion = doc.getString("descripcion")
                                    val colorHex = doc.getString("color") ?: "000000"
                                    val colorCategoriaHex = doc.getString("color_categoria")
                                    val esFavorito = doc.getBoolean("favorito") ?: false
                                    val listaCompartidos =
                                        (doc.get("compartido_con") as? List<String>) ?: emptyList()
                                    val estaCompartido = listaCompartidos.isNotEmpty()
                                    val compartidoPor = doc.getString("creador")
                                    val id = doc.id

                                        Recordatorio(
                                            id = id,
                                            titulo = titulo,
                                            fecha = fecha,
                                            descripcion = descripcion,
                                            color = colorHex ,
                                            color_de_la_categoria = colorCategoriaHex ,
                                            esFavorito = esFavorito,
                                            esta_Compartido = estaCompartido,
                                            lista_compartidos = listaCompartidos,
                                            compartidoPor = compartidoPor
                                        )

                                }
                                _recordatorios.clear()
                                _recordatorios.addAll(lista)
                            }
                    } else {
                        _recordatorios.clear()
                    }
                }
        }
    }

    fun actualizarFavorito(recordatorio: Recordatorio) {
        val userId = auth.currentUser?.uid
        val recordatorioId = recordatorio.id
        if (userId != null && recordatorioId != null) {
            val esFavoritoActual = recordatorio.esFavorito == true
            val nuevoFavorito = !esFavoritoActual

            actualizarCampoEnFirestore(recordatorioId, "favorito", nuevoFavorito)

            _recordatorios.find { it.id == recordatorioId }?.esFavorito = nuevoFavorito
        }
    }


    fun actualizarRecordatorio(recordatorio: Recordatorio) {
        val userId = auth.currentUser?.uid
        val recordatorioId = recordatorio.id
        if (userId != null && recordatorioId != null) {
            firestore.collection("Users").document(userId)
                .collection("Reminders").document(recordatorioId)
                .set(recordatorio)
                .addOnSuccessListener {
                    cargarRecordatorios()
                }
        }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    private fun actualizarCampoEnFirestore(recordatorioId: String, campo: String, valor: Any) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            firestore.collection("Users").document(userId)
                .collection("Reminder").document(recordatorioId)
                .update(campo, valor)
        }
    }
}