package com.example.proyecto_gestion_de_recordatorios.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyecto_gestion_de_recordatorios.ui.theme.background_home
import com.example.proyecto_gestion_de_recordatorios.ui.theme.background_rnrfc
import com.example.proyecto_gestion_de_recordatorios.ui.theme.bar
import com.example.proyecto_gestion_de_recordatorios.ui.theme.floating_button_reminder
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun HomeScreen() {
    val reminderList = listOf(
        Reminder("Comprar leche", "Compras", "Alta", "09:00"),
        Reminder("Estudiar Kotlin", "Estudios", "Media", "16:30"),
        Reminder("Llamar al médico", "Personal", "Baja", "18:00")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Recordatorios", color = Color.Black) }, modifier = Modifier.background(bar)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* Navegar a NewReminderScreen */ },
                containerColor = floating_button_reminder
            ) {
                Icon(Icons.Default.Add, contentDescription = "Añadir recordatorio", tint = Color.White)
            }
        },
        containerColor = background_home
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(background_home)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            items(reminderList) { reminder ->
                ReminderCard(reminder)
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun ReminderCard(reminder: Reminder) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = background_rnrfc),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = reminder.title, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Categoría: ${reminder.category}")
            Text("Prioridad: ${reminder.priority}")
            Text("Hora: ${reminder.time}")
        }
    }
}

data class Reminder(
    val title: String,
    val category: String,
    val priority: String,
    val time: String
)