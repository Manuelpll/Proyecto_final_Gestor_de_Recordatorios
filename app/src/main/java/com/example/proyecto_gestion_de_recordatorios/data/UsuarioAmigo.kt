package com.example.proyecto_gestion_de_recordatorios.data

import com.google.firebase.firestore.DocumentReference

data class UsuarioAmigo(
    val referencia: DocumentReference,
    val nombre: String,
    val imagenUrl: String
)
