package com.example.proyecto_gestion_de_recordatorios.data


data class Recordatorio(
    val id:String? = null,
    val titulo: String? = null,
    val fecha: String? = null,
    val descripcion: String? = null,
    val prioridad:String?= null,
    val color: String,
    val color_de_la_categoria: String? = null,
    var esFavorito:Boolean?= null,
    val esEditable:Boolean?= null,
    val esta_Compartido: Boolean? = null,
    val lista_compartidos:List<String>? = null,
    val compartidoPor: String? = null,
)