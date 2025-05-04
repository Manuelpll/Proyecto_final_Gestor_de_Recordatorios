package com.example.proyecto_gestion_de_recordatorios.data

data class Usuario(
    val nombre:String,
    val email:String?=null,
    val telefono:Int?=null,
    val ubicacion:String?=null,
    val contactos:List<String>? = null,
    val recordatorios_disponibles:List<String>? = null
)
