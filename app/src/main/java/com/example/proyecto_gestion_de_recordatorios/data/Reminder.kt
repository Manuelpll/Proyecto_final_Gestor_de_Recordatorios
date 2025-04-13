package com.example.proyecto_gestion_de_recordatorios.data

import androidx.compose.ui.graphics.Color

data class Reminder(
    val title: String,
    val category: String,
    val date: String,
    val backgroundColor: Color,
    val categoryColor: Color
)
