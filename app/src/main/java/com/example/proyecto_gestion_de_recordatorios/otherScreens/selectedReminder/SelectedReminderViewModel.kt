package com.example.proyecto_gestion_de_recordatorios.otherScreens.selectedReminder

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto_gestion_de_recordatorios.data.Recordatorio
import com.example.proyecto_gestion_de_recordatorios.data.UsuarioAmigo
import com.example.proyecto_gestion_de_recordatorios.otherScreens.newReminder.ReminderReceiver
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

/**
 * ViewModel de la SelectedReminderScreen
 */
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

    private val _contactosAmigos = mutableStateListOf<UsuarioAmigo>()
    val contactosAmigos: List<UsuarioAmigo> = _contactosAmigos

    private val _seleccionados = mutableStateOf<List<DocumentReference>>(emptyList())
    val amigosSeleccionados: List<DocumentReference> get() = _seleccionados.value

    var nombreUsuarioActual by mutableStateOf("")
        private set

    init {
        obtenerNombreUsuarioActual()
    }

    private fun obtenerNombreUsuarioActual() {
        val uid = auth.currentUser?.uid ?: return
        firestore.collection("Users")
            .document(uid)
            .get()
            .addOnSuccessListener { documento ->
                nombreUsuarioActual = documento.getString("nombre") ?: "Usuario"
            }
            .addOnFailureListener {
                Log.e("ViewModel", "Error al obtener nombre del usuario: ${it.message}")
            }
    }
    fun loadReminderById(id: String) {
        viewModelScope.launch {
            try {
                val uid = auth.currentUser?.uid ?: return@launch

                val userSnapshot = firestore.collection("Users")
                    .document(uid)
                    .get()
                    .await()

                val recordatoriosDisponiblesRefs = userSnapshot.get("recordatorios_disponibles") as? List<DocumentReference> ?: emptyList()

                val rutaRecordatorio = recordatoriosDisponiblesRefs.find { it.id == id }?.path

                if (rutaRecordatorio != null) {
                    val snapshot = firestore.document(rutaRecordatorio)
                        .get()
                        .await()

                    if (snapshot.exists()) {
                        val data = snapshot.data
                        if (data != null) {
                            val recordatorio = Recordatorio(
                                id = data["id"] as? String ?: "",
                                titulo = data["titulo"] as? String ?: "",
                                descripcion = data["descripcion"] as? String ?: "",
                                fecha = data["fecha"] as? String ?: "",
                                prioridad = data["prioridad"] as? String ?: "",
                                color = data["color"] as? String ?: "",
                                color_de_la_categoria = data["color_de_la_categoria"] as? String ?: "",
                                esFavorito = data["esFavorito"] as? Boolean ?: false,
                                esta_Compartido = data["esta_Compartido"] as? Boolean ?: false,
                                compartidoPor = data["compartidoPor"] as? String ?: ""
                            )
                            _selectedReminder.value = recordatorio
                        } else {
                            Log.e("ViewModel", "No se encontraron datos en la ruta: $rutaRecordatorio")
                            _selectedReminder.value = null
                        }
                    } else {
                        Log.e("ViewModel", "No se encontró el recordatorio en la ruta: $rutaRecordatorio")
                        _selectedReminder.value = null
                    }
                } else {
                    Log.e("ViewModel", "El id $id no está disponible para el usuario actual.")
                    _selectedReminder.value = null
                }
            } catch (e: Exception) {
                Log.e("ViewModel", "Error al cargar recordatorio: ${e.message}")
                _selectedReminder.value = null
            }
        }
    }
//Metodo que edita algunos campos del recordatorio
    fun editarTituloYDescripcion(
        nuevoTitulo: String,
        nuevaDescripcion: String,
        id_recordatorio: String
    ) {
        val uid = auth.currentUser?.uid ?: return

        if (id_recordatorio.isBlank()) {
            Log.e("ViewModel", "ID del recordatorio recibido es nulo o vacío. No se puede actualizar.")
            return
        }

        val reminderRef = firestore.collection("Users")
            .document(uid)
            .collection("Reminders")
            .document(id_recordatorio)

        viewModelScope.launch {
            try {
                reminderRef.update(
                    mapOf(
                        "titulo" to nuevoTitulo,
                        "descripcion" to nuevaDescripcion
                    )
                ).await()
                loadReminderById(id_recordatorio)
            } catch (e: Exception) {
                Log.e("ViewModel", "Error al editar recordatorio: ${e.message}")
            }
        }
    }

    fun loadProfilePhoto() {
        viewModelScope.launch {
            try {
                val uid = auth.currentUser?.uid ?: return@launch
                val ref = storage.reference.child("profile_images/$uid.jpg")
                _profilePhotoUrl.value = ref.downloadUrl.await().toString()
            } catch (e: Exception) {
                _profilePhotoUrl.value = null
            }
        }
    }
//Metodo que obtiene los contactos del usuario para poder elegirlos a la hora de compartir el recordatorio
    fun obtenerContactos() {
        val userId = auth.currentUser?.uid ?: return
        val usuarioDocRef = firestore.collection("Users").document(userId)

        usuarioDocRef.get().addOnSuccessListener { doc ->
            val contactosRaw = doc["contactos"] as? List<*>
            val contactos: List<DocumentReference> = (contactosRaw?.mapNotNull {
                when (it) {
                    is DocumentReference -> it
                    is String -> {
                        if (!it.contains("/")) {
                            firestore.collection("Users").document(it)
                        } else {
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
                    val storageRef = storage.reference.child("profile_images/$amigoId.jpg")

                    storageRef.downloadUrl.addOnSuccessListener { imagenUrl ->
                        val amigo = UsuarioAmigo(
                            nombre = nombre,
                            imagenUrl = imagenUrl.toString(),
                            referencia = ref.path
                        )
                        _contactosAmigos.add(amigo)
                    }.addOnFailureListener {
                        val amigo = UsuarioAmigo(nombre = nombre, imagenUrl = "", referencia = ref.path)
                        _contactosAmigos.add(amigo)
                    }
                }
            }
        }
    }
//Metodo que selecciona el contacto al que quieres compartir
    fun toggleSeleccionAmigo(amigoRef: String) {
        val currentList = _seleccionados.value.toMutableList()

        val path = if (amigoRef.contains("/")) amigoRef else "Users/$amigoRef"
        val refAmigo = firestore.document(path)

        val index = currentList.indexOfFirst { it.id == refAmigo.id }
        if (index != -1) {
            currentList.removeAt(index)
        } else {
            currentList.add(refAmigo)
        }
        _seleccionados.value = currentList
    }

    fun limpiarSeleccionAmigos() {
        _seleccionados.value = emptyList()
    }

    fun compartirRecordatorio(
        recordatorio: Recordatorio,
        context: Context,
        onCompartido: () -> Unit,
    ) {
        val userId = auth.currentUser?.uid ?: return
        val recordatorioId = recordatorio.id ?: return

        if (recordatorio.compartidoPor != userId && (recordatorio.compartidoPor?.isNotBlank() == true)) {
            Log.w("compartirRecordatorio", "No puedes compartir un recordatorio que no creaste.")
            return
        }

        val usuarioRef = firestore.collection("Users").document(userId)
        usuarioRef.get().addOnSuccessListener { usuarioSnapshot ->
            if (!usuarioSnapshot.exists()) {
                Log.e("compartirRecordatorio", "El usuario no existe en Firestore.")
                return@addOnSuccessListener
            }

            val recordatorioRef = usuarioRef
                .collection("Reminders")
                .document(recordatorioId)

            if (amigosSeleccionados.isEmpty()) {
                Log.w("compartirRecordatorio", "No hay amigos seleccionados. No se compartira a nadie.")
            }

            amigosSeleccionados.forEach { amigoRef ->
                val amigoDocRef = firestore.collection("Users").document(amigoRef.id)

                amigoDocRef.update("recordatorios_disponibles", FieldValue.arrayUnion(recordatorioRef))
                    .addOnSuccessListener {
                    }
                    .addOnFailureListener { e ->
                        Log.e("compartirRecordatorio", "Error al añadir a recordatorios_disponibles de ${amigoRef.id}: ${e.message}")
                    }


                val notificacion = mapOf(
                    "descripcion" to (recordatorio.titulo ?: "Recordatorio sin título"),
                    "usuario" to amigoRef,
                    "recordatorio" to recordatorioRef,
                    "fechaCreacion" to FieldValue.serverTimestamp()
                )

                firestore.collection("Notification").add(notificacion)
                    .addOnSuccessListener { doc ->
                    }
                    .addOnFailureListener { e ->
                        Log.e("compartirRecordatorio", "Error al crear notificación para ${amigoRef.id}: ${e.message}")
                    }


                recordatorioRef.update("lista_compartidos", FieldValue.arrayUnion(amigoRef))
                    .addOnSuccessListener {
                    }
                    .addOnFailureListener { e ->
                        Log.e("compartirRecordatorio", "Error al añadir a lista_compartidos: ${e.message}")
                    }

                programarNotificacion(context, recordatorio, amigoRef.id.hashCode())
            }

            recordatorioRef.update("esta_Compartido", true)
                .addOnSuccessListener {
                }
                .addOnFailureListener { e ->
                    Log.e("compartirRecordatorio", "Error al actualizar esta_Compartido: ${e.message}")
                }

            limpiarSeleccionAmigos()
            onCompartido()
        }.addOnFailureListener { e ->
            Log.e("compartirRecordatorio", "Error al obtener datos del usuario: ${e.message}")
        }
    }
//Metodo que programa la notificacion para otros usuarios
    @SuppressLint("ScheduleExactAlarm")
    private fun programarNotificacion(context: Context, recordatorio: Recordatorio, uniqueId: Int) {
        val fechaString = recordatorio.fecha
        if (fechaString.isNullOrBlank()) {
            Toast.makeText(context, "Error: fecha no válida", Toast.LENGTH_SHORT).show()
            return
        }

        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        val fecha = try {
            sdf.parse(fechaString)
        } catch (e: ParseException) {
            null
        }

        if (fecha == null) {
            Toast.makeText(context, "Error al parsear la fecha", Toast.LENGTH_SHORT).show()
            return
        }

        val horaRecordatorio = fecha.time
        val horaNotificacion = horaRecordatorio - 2 * 60 * 60 * 1000

        val intent = Intent(context, ReminderReceiver::class.java).apply {
            putExtra("titulo", recordatorio.titulo)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            uniqueId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, horaNotificacion, pendingIntent)
    }
}