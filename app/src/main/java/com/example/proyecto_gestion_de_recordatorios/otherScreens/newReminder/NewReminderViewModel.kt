package com.example.proyecto_gestion_de_recordatorios.otherScreens.newReminder

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto_gestion_de_recordatorios.data.Recordatorio
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class NewReminderViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    @ApplicationContext private val context: Context
) : ViewModel() {

    var titulo by mutableStateOf("")
    var descripcion by mutableStateOf("")
    var fechaHora by mutableStateOf("2025-05-18 20:40")
    var colorIndex by mutableStateOf(0f)
    var prioridadIndex by mutableStateOf(0f)
    var esEditable by mutableStateOf(false)
    var categoriaSeleccionada: String? by mutableStateOf(null)
    var categoriasDisponibles by mutableStateOf<Map<String, Color>>(emptyMap())

    val colores = listOf(
        // Rojos
        Color(0xFFFFCDD2),
        Color(0xFFEF9A9A),
        Color(0xFFE57373),
        Color(0xFFEF5350),
        Color(0xFFF44336),
        Color(0xFFE53935),
        Color(0xFFD32F2F),
        Color(0xFFC62828),
        Color(0xFFB71C1C),
        Color(0xFFFF8A80),

        // Naranjas
        Color(0xFFFFE0B2),
        Color(0xFFFFCC80),
        Color(0xFFFFB74D),
        Color(0xFFFFA726),
        Color(0xFFFF9800),
        Color(0xFFFB8C00),
        Color(0xFFF57C00),
        Color(0xFFEF6C00),
        Color(0xFFE65100),
        Color(0xFFFFAB91),

        // Amarillos
        Color(0xFFFFF9C4),
        Color(0xFFFFF59D),
        Color(0xFFFFF176),
        Color(0xFFFFEE58),
        Color(0xFFFFEB3B),
        Color(0xFFFDD835),
        Color(0xFFFBC02D),
        Color(0xFFF9A825),
        Color(0xFFF57F17),
        Color(0xFFFFF176),

        // Verdes
        Color(0xFFC8E6C9),
        Color(0xFFA5D6A7),
        Color(0xFF81C784),
        Color(0xFF66BB6A),
        Color(0xFF4CAF50),
        Color(0xFF43A047),
        Color(0xFF388E3C),
        Color(0xFF2E7D32),
        Color(0xFF1B5E20),
        Color(0xFFAED581),

        // Azules
        Color(0xFFBBDEFB),
        Color(0xFF90CAF9),
        Color(0xFF64B5F6),
        Color(0xFF42A5F5),
        Color(0xFF2196F3),
        Color(0xFF1E88E5),
        Color(0xFF1976D2),
        Color(0xFF1565C0),
        Color(0xFF0D47A1),
        Color(0xFF82B1FF),

        // Morados
        Color(0xFFE1BEE7),
        Color(0xFFCE93D8),
        Color(0xFFBA68C8),
        Color(0xFFAB47BC),
        Color(0xFF9C27B0),
        Color(0xFF8E24AA),
        Color(0xFF7B1FA2),
        Color(0xFF6A1B9A),
        Color(0xFF4A148C),
        Color(0xFFB39DDB),

        // Neutros / Grises
        Color(0xFFF5F5F5),
        Color(0xFFEEEEEE),
        Color(0xFFE0E0E0),
        Color(0xFFBDBDBD),
        Color(0xFF9E9E9E),
        Color(0xFF757575),
        Color(0xFF616161),
        Color(0xFF424242),
        Color(0xFF212121),
        Color(0xFF000000)
    )

    val prioridades = listOf("Baja", "Media", "Alta")

    fun guardarRecordatorio(
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val uid = auth.currentUser?.uid
        if (uid == null) {
            onFailure(Exception("Usuario no autenticado"))
            return
        }
        if (titulo.isBlank()) {
            onFailure(Exception("El título no puede estar vacío."))
            return
        }

        if (descripcion.isBlank()) {
            onFailure(Exception("La descripción no puede estar vacía."))
            return
        }

        val colorSeleccionado: Color = colores[colorIndex.toInt()]
        val colorHex = String.format("#%06X", 0xFFFFFF and colorSeleccionado.toArgb())
        val prioridadSeleccionada = prioridades[prioridadIndex.toInt()]
        val colorCategoria = categoriaSeleccionada?.let {
            categoriasDisponibles[it]?.toArgb()?.toString()
        } ?: ""
        val recordatorio = Recordatorio(
            titulo = titulo,
            descripcion = descripcion,
            fecha = fechaHora,
            prioridad = prioridadSeleccionada,
            color = colorHex,
            color_de_la_categoria = colorCategoria,
            esFavorito = false,
            esEditable = esEditable,
            esta_Compartido = false,
            lista_compartidos = emptyList(),
            compartidoPor = null
        )

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val userDoc = firestore.collection("Users").document(uid)
                val userSnapshot = userDoc.get().await()

                if (!userSnapshot.exists()) {
                    userDoc.set(hashMapOf("creado" to true)).await()
                }

                val docRef = userDoc.collection("Reminders").add(recordatorio).await()

                userDoc.update("recordatorios_disponibles", FieldValue.arrayUnion(docRef)).await()

                firestore.collection("Notification").add(
                    mapOf(
                        "descripcion" to descripcion,
                        "usuario" to userDoc,
                        "recordatorio" to docRef,
                        "fechaCreacion" to FieldValue.serverTimestamp()
                    )
                ).await()

                withContext(Dispatchers.Main) {
                    programarNotificacion(context, recordatorio)
                    onSuccess()
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onFailure(e)
                }
            }
        }
    }

    fun cargarCategorias() {
        firestore.collection("Categories")
            .whereEqualTo("usuarioId", auth.uid)
            .get()
            .addOnSuccessListener { snapshot ->
                val categorias = mutableMapOf<String, Color>()
                for (document in snapshot.documents) {
                    val nombre = document.getString("nombre") ?: continue
                    val colorHex = document.getString("color")
                    if (!colorHex.isNullOrBlank()) {
                        try {
                            val colorInt = Color(android.graphics.Color.parseColor(colorHex))
                            categorias[nombre] = colorInt
                        } catch (_: IllegalArgumentException) {
                            Log.w("CategoriaInvalida", "Categoría '$nombre' con color inválido: '$colorHex'")
                            categorias[nombre] = Color(0xFF000000) // Color por defecto si parsea mal
                        }
                    } else {
                        Log.w("CategoriaInvalida", "Categoría '$nombre' con color inválido: '$colorHex'")
                        categorias[nombre] = Color(0xFF000000) // Color por defecto si está vacío o nulo
                    }
                }
                categoriasDisponibles = categorias
            }
    }

}

    @SuppressLint("ScheduleExactAlarm")
    private fun programarNotificacion(context: Context, recordatorio: Recordatorio) {
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
            recordatorio.titulo.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, horaNotificacion, pendingIntent)
    }