package com.example.proyecto_gestion_de_recordatorios.data


data class Recordatorio(
    val id: String = "",
    val titulo: String? = "",
    val fecha: String? = "",
    val descripcion: String? = "",
    val prioridad: String = "",
    val color: String = "",
    val color_de_la_categoria: String? = "",
    var esFavorito: Boolean = false,
    var esEditable: Boolean = false,
    val esta_Compartido: Boolean = false,
    val lista_compartidos: List<String> = emptyList(),
    val compartidoPor: String? = null
)