package com.example.proyecto_gestion_de_recordatorios.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.example.proyecto_gestion_de_recordatorios.ui.theme.bar
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
import com.example.proyecto_gestion_de_recordatorios.data.Recordatorio


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navegateToReminder: () -> Unit,
    navegateToFriends: () -> Unit,
    navegateToCategory: () -> Unit,
    navegateToSelectedReminder: () -> Unit,
    navegateToProfile: () -> Unit
) {
    val recordatorioLists = listOf(
        Recordatorio("Charla formación", "Trabajo", "4/04", Color(0xFFF1C4BC), Color.Red),
        Recordatorio("Charla formación", "Salud", "4/04", Color(0xFF4CAF50), Color.Yellow),
        Recordatorio("Charla formación", "Educación", "4/04", Color(0xFFB2EBF2), Color.Cyan),
        Recordatorio("Charla formación", "Reunión", "4/04", Color(0xFF9575CD), Color.Magenta),
        Recordatorio("Charla formación", "Evento", "4/04", Color(0xFFFFEB3B), Color.Green)
    )
    var expanded by remember { mutableStateOf(false)}

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.background(bar),
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box {
                            IconButton(onClick = { expanded = true }) {
                                Icon(Icons.Default.Menu, contentDescription = "Menú", tint = Color.Black)
                            }
                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Mis Recordatorios") },
                                    onClick = { expanded = false /* Acción */ }
                                )
                                DropdownMenuItem(
                                    text = { Text("Amigos") },
                                    onClick = { expanded = false /* Acción */ }
                                )
                                DropdownMenuItem(
                                    text = { Text("Categorías Creadas") },
                                    onClick = { expanded = false /* Acción */ }
                                )
                            }
                        }
                        Text(
                            text = "Home",
                            color = Color.Black,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center
                        )
                        IconButton(onClick = { navegateToProfile()}, modifier = Modifier.size(70.dp)){
                        Icon(
                            Icons.Default.AccountCircle,
                            contentDescription = "Perfil",
                            modifier = Modifier.size(70.dp)
                                .padding(end= 20.dp),
                            tint = Color.Black
                        )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = bar)
            )
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
            items(recordatorioLists) { reminder2 ->
                ReminderCard(reminder2)
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun ReminderCard(recordatorio: Recordatorio) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxSize(),
            colors = CardDefaults.cardColors(
                containerColor = recordatorio.color
            ),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                recordatorio.titulo?.let {
                    Text(
                        text = it,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                recordatorio.fecha?.let {
                    Text(
                        text = it,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(4.dp)
                .size(12.dp)
                .background(recordatorio.color_de_la_categoria!!, shape = RectangleShape)
        )
    }
}