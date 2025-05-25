package com.example.proyecto_gestion_de_recordatorios.otherScreens.reminder

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.proyecto_gestion_de_recordatorios.data.Recordatorio
import com.example.proyecto_gestion_de_recordatorios.data.UsuarioAmigo
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.SetOptions
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

@HiltViewModel
class ReminderViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) : ViewModel() {

    private val _recordatorios = mutableStateListOf<Recordatorio>()
    val recordatorios: List<Recordatorio> = _recordatorios

    var searchQuery = mutableStateOf("")
        private set

    var fotoPerfilUrl by mutableStateOf<String?>(null)
        private set

    private val _contactosAmigos = mutableStateListOf<UsuarioAmigo>()
    val contactosAmigos: List<UsuarioAmigo> = _contactosAmigos

    private val _seleccionados = mutableStateOf<List<DocumentReference>>(emptyList())
    val amigosSeleccionados: List<DocumentReference> get() = _seleccionados.value

    fun onSearchQueryChange(query: String) {
        searchQuery.value = query
    }

    fun cargarRecordatorios() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            firestore.collection("Users")
                .document(userId)
                .collection("Reminders")
                .get()
                .addOnSuccessListener { result ->
                    _recordatorios.clear()
                    for (doc in result) {
                        val recordatorio = doc.toObject(Recordatorio::class.java).copy(id = doc.id)
                        _recordatorios.add(recordatorio)
                    }
                }
        }
    }

    fun actualizarFavorito(recordatorio: Recordatorio) {
        val userId = auth.currentUser?.uid
        val nuevoEstado = !(recordatorio.esFavorito ?: false)

        if (userId != null) {
            firestore.collection("Users")
                .document(userId)
                .collection("Reminders")
                .document(recordatorio.id ?: return)
                .update("esFavorito", nuevoEstado)
                .addOnSuccessListener {
                    cargarRecordatorios()
                }
        }
    }

    fun cargarImagenPerfil() {
        val userId = auth.currentUser?.uid
        val ref = storage.reference.child("FotosPerfil/$userId.jpg")
        ref.downloadUrl.addOnSuccessListener { url ->
            fotoPerfilUrl = url.toString()
        }
    }

    fun obtenerContactos() {
        val userId = auth.currentUser?.uid ?: return
        val usuarioDocRef = firestore.collection("Users").document(userId)

        usuarioDocRef.get().addOnSuccessListener { doc ->
            val contactosRaw = doc["contactos"] as? List<*>
            val contactos: List<DocumentReference> = (contactosRaw?.mapNotNull {
                when (it) {
                    is DocumentReference -> it
                    is String -> {
                        // Verifica si es solo el ID (sin "/")
                        if (!it.contains("/")) {
                            // Construye el path completo manualmente
                            firestore.collection("Users").document(it)
                        } else {
                            // Ya es un path completo
                            firestore.document(it)
                        }
                    }

                    else -> null
                }
            } ?: _contactosAmigos.clear()) as List<DocumentReference>

            contactos.forEach { ref ->
                ref.get().addOnSuccessListener { amigoDoc ->
                    val nombre = amigoDoc.getString("nombre") ?: "Desconocido"
                    val amigoId = ref.id
                    val storageRef = storage.reference.child("FotosPerfil/$amigoId.jpg")

                    storageRef.downloadUrl.addOnSuccessListener { imagenUrl ->
                        val amigo = UsuarioAmigo(nombre = nombre, imagenUrl = imagenUrl.toString(), referencia = ref)
                        _contactosAmigos.add(amigo)
                    }.addOnFailureListener {
                        val amigo = UsuarioAmigo(nombre = nombre, imagenUrl = "", referencia = ref)
                        _contactosAmigos.add(amigo)
                    }
                }
            }
        }
    }

    fun toggleSeleccionAmigo(amigoRef: DocumentReference) {
        _seleccionados.value = if (_seleccionados.value.contains(amigoRef)) {
            _seleccionados.value - amigoRef
        } else {
            _seleccionados.value + amigoRef
        }
    }

    fun limpiarSeleccionAmigos() {
        _seleccionados.value = emptyList()
    }

    fun compartirRecordatorio(
        recordatorio: Recordatorio,
        onCompartido: () -> Unit
    ) {
        val userId = auth.currentUser?.uid ?: return
        val recordatorioId = recordatorio.id ?: return

        val recordatorioRef = firestore.collection("Users")
            .document(userId)
            .collection("Reminders")
            .document(recordatorioId)

        amigosSeleccionados.forEach { amigoRef ->
            // Añadir referencia al array "recordatorios_disponibles"
            amigoRef.update("recordatorios_disponibles", FieldValue.arrayUnion(recordatorioRef))

            // Crear notificación para el amigo
            val notificacion = hashMapOf(
                "descripcion" to "Te han compartido un recordatorio.",
                "usuario" to amigoRef,
                "recordatorio" to recordatorioRef
            )

            firestore.collection("Notification").add(notificacion)
        }

        // Limpiar selección y notificar
        limpiarSeleccionAmigos()
        onCompartido()
    }
}