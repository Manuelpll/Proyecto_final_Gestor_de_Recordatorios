package com.example.proyecto_gestion_de_recordatorios.data

import androidx.compose.ui.graphics.Color

data class Recordatorio(
    val titulo: String? = null,
    val fecha: String? = null,
    val descripcion: String? = null,
    val color: Color,
    val color_de_la_categoria: Color? = null,
    val esFavorito:Boolean?= null,
    val esta_Compartido: Boolean? = null,
    val lista_compartidos:List<String>? = null,
    val compartidoPor: String? = null,
)