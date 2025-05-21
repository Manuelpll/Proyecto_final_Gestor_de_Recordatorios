package com.example.proyecto_gestion_de_recordatorios.otherScreens.newCategory

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NewCategoryViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val user:FirebaseAuth
) : ViewModel() {

    var nombre by mutableStateOf("")
        private set

    var colorIndex by mutableStateOf(0f)
        private set

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
        Color(0xFF000000) )

    fun onNombreChange(nuevo: String) {
        nombre = nuevo
    }

    fun onColorIndexChange(nuevo: Float) {
        colorIndex = nuevo
    }

    fun crearCategoria(onSuccess: () -> Unit) {
        val userId =user.currentUser?.uid
        if (userId == null) {
            Log.e("NuevaCategoria", "Usuario no autenticado.")
            return
        }

        val recordatorios_categoria= emptyList<String>()

        val categoria = hashMapOf(
            "nombre" to nombre,
            "color" to colores[colorIndex.toInt()].toArgb().toUInt().toString(16),
            "usuarioId" to userId,
            "recordatorios_pertenecientes" to  recordatorios_categoria
        )

        firestore.collection("Categories")
            .add(categoria)
            .addOnSuccessListener {Log.d("FirestoreDebug", "Categoría creada correctamente")
                onSuccess() }
            .addOnFailureListener { e -> Log.e("NuevaCategoria", "Error al guardar: ${e.message}") }
    }
}