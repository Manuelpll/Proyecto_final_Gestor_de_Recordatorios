package com.example.proyecto_gestion_de_recordatorios.otherScreens.reminder

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.proyecto_gestion_de_recordatorios.data.Recordatorio
import com.example.proyecto_gestion_de_recordatorios.data.UsuarioAmigo
import com.example.proyecto_gestion_de_recordatorios.otherScreens.newReminder.ReminderReceiver
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import dagger.hilt.android.qualifiers.ApplicationContext
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale


@HiltViewModel
class ReminderViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage,
    @ApplicationContext private val context: Context
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

    fun obtenerTodosLosRecordatorios() {
        val userId = auth.currentUser?.uid ?: return

        val userDocRef = firestore.collection("Users").document(userId)

        userDocRef.get().addOnSuccessListener { userSnapshot ->
            val recordatoriosCompartidosRefs =
                userSnapshot.get("recordatorios_disponibles") as? List<DocumentReference> ?: emptyList()

            firestore.collection("Users").document(userId).collection("Reminders")
                .get()
                .addOnSuccessListener { propiosSnapshot ->

                    val propiosList = propiosSnapshot.documents.mapNotNull { doc ->
                        docToRecordatorio(doc)
                    }.toMutableList()

                    val recordatoriosIds = propiosList.map { it.id }.toMutableSet()

                    if (recordatoriosCompartidosRefs.isNotEmpty()) {
                        val tareas = recordatoriosCompartidosRefs.map { ref -> ref.get() }

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
                    obtenerTodosLosRecordatorios()
                }
        }
    }

    fun cargarImagenPerfil() {
        val userId = auth.currentUser?.uid
        val ref = storage.reference.child("profile_images/$userId.jpg")
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

        Log.d("toggleSeleccionAmigo", "Amigos seleccionados actuales: ${_seleccionados.value.map { it.id }}")
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
            Log.d("compartirRecordatorio", "Amigos seleccionados: $amigosSeleccionados")

            if (amigosSeleccionados.isEmpty()) {
                Log.w("compartirRecordatorio", "No hay amigos seleccionados. No se compartira a nadie.")
            }

            amigosSeleccionados.forEach { amigoRef ->
                val amigoDocRef = firestore.collection("Users").document(amigoRef.id)

                amigoDocRef.update("recordatorios_disponibles", FieldValue.arrayUnion(recordatorioRef))
                    .addOnSuccessListener {
                        Log.d("compartirRecordatorio", "Añadido a recordatorios_disponibles de ${amigoRef.id}")
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
                        Log.d("compartirRecordatorio", "Notificación creada para ${amigoRef.id}: ${doc.id}")
                    }
                    .addOnFailureListener { e ->
                        Log.e("compartirRecordatorio", "Error al crear notificación para ${amigoRef.id}: ${e.message}")
                    }


                recordatorioRef.update("lista_compartidos", FieldValue.arrayUnion(amigoRef))
                    .addOnSuccessListener {
                        Log.d("compartirRecordatorio", "Añadido ${amigoRef.id} a lista_compartidos de $recordatorioId")
                    }
                    .addOnFailureListener { e ->
                        Log.e("compartirRecordatorio", "Error al añadir a lista_compartidos: ${e.message}")
                    }

                programarNotificacion(context, recordatorio, amigoRef.id.hashCode())
                Log.d("compartirRecordatorio", "Notificación programada para ${amigoRef.id}")
            }

            recordatorioRef.update("esta_Compartido", true)
                .addOnSuccessListener {
                    Log.d("compartirRecordatorio", "Recordatorio $recordatorioId marcado como compartido")
                }
                .addOnFailureListener { e ->
                    Log.e("compartirRecordatorio", "Error al actualizar esta_Compartido: ${e.message}")
                }

            limpiarSeleccionAmigos()
            Log.d("compartirRecordatorio", "Selección de amigos limpiada")
            onCompartido()
            Log.d("compartirRecordatorio", "Proceso completado correctamente")
        }.addOnFailureListener { e ->
            Log.e("compartirRecordatorio", "Error al obtener datos del usuario: ${e.message}")
        }
    }

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