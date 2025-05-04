package com.example.proyecto_gestion_de_recordatorios.otherScreens.reminder

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyecto_gestion_de_recordatorios.data.Recordatorio
import com.example.proyecto_gestion_de_recordatorios.ui.theme.background_rnrfc
import com.example.proyecto_gestion_de_recordatorios.ui.theme.bar
import com.example.proyecto_gestion_de_recordatorios.ui.theme.floating_button_reminder
import com.example.proyecto_gestion_de_recordatorios.ui.theme.reminder_compatir

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ReminderScreen(
    navegateToNewReminder: () -> Unit,
    navegateToSelectedReminder: () -> Unit,
    navegateToProfile: () -> Unit,
    navegateToCategory: () -> Unit,
    navegateToFriend: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val recordatorios = listOf(
        Recordatorio("Reunión", "13/05/2025", "En la reunión vamos a comentar sobre el balance semanal", Color.LightGray, Color.Red),
        Recordatorio("Conferencia", "10/05/2025", "Vamos a comentar sobre el balance semanal sobre e...", Color.LightGray, Color.Blue, compartidoPor = "Pablo", esta_Compartido = true),
        Recordatorio("Peluquería", "07/05/2025", "En la reunión vamos a comentar sobre el balance semanal", Color.LightGray, Color.Green)
    )
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
                                Icon(
                                    Icons.Default.Menu,
                                    contentDescription = "Menú",
                                    tint = Color.Black
                                )
                            }
                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Home") },
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
                            text = "Mis Recordatorios",
                            color = Color.Black,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center
                        )
                        IconButton(onClick = {}, modifier = Modifier.size(70.dp)) {
                            Icon(
                                Icons.Default.AccountCircle,
                                contentDescription = "Perfil",
                                modifier = Modifier.size(70.dp)
                                    .padding(end = 20.dp),
                                tint = Color.Black
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = bar)
            )
        },
        containerColor = background_rnrfc,
        bottomBar = {
            BottomAppBar(
                containerColor = bar,
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                IconButton(onClick = { /* Acción de volver */ }) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver",
                        tint = Color.Black
                    )
                }
            }
        }, floatingActionButton = {
            FloatingActionButton(
                onClick = { /* Acción añadir */ },
                containerColor = floating_button_reminder
            ) {
                Icon(Icons.Default.Add, contentDescription = "Añadir", tint = Color.Black)
            }
        }
    ) { innerPadding ->
        var searchText by remember { mutableStateOf("") }

        Column(modifier = Modifier.padding(16.dp)) {
            // Campo de búsqueda
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                placeholder = { Text("Buscar Recordatorio") },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = "Buscar")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, shape = RoundedCornerShape(10.dp))
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Lista de recordatorios filtrada
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(recordatorios.filter {
                    it.titulo?.contains(searchText, ignoreCase = true) == true
                }) { recordatorio ->
                    ReminderCard(recordatorio)
                }
            }
        }
    }
}


@Composable
fun ReminderCard(recordatorio: Recordatorio) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFE0DEDE), RoundedCornerShape(8.dp))
            .padding(12.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Favorito",
                        tint = Color.Black
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = recordatorio.titulo ?: "Sin título",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }

                Text(
                    text = recordatorio.fecha ?: "",
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = recordatorio.descripcion ?: "",
                color = Color.Red,
                fontSize = 14.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = { /* Acción compartir */ },
                    colors = ButtonDefaults.buttonColors(containerColor = reminder_compatir),
                    shape = RoundedCornerShape(20.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp)
                ) {
                    Text("Compartir", fontSize = 12.sp, color = Color.Black)
                }

                Row {
                    // Mostrar autor si está compartido por otro
                    recordatorio.compartidoPor?.let {
                        Text(
                            text = "Compartido por $it",
                            fontSize = 12.sp,
                            color = Color.DarkGray
                        )
                    }

                    // Mostrar ícono de edición si no está compartido o si es el creador
                    if (recordatorio.esta_Compartido != true) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "Editar",
                            tint = Color.Black,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            // Indicador de color de categoría (esquina superior derecha)
            Box(
                modifier = Modifier
                    .align(Alignment.End)
                    .offset(y = (-48).dp, x = 4.dp)
                    .size(16.dp)
                    .background(recordatorio.color_de_la_categoria ?: Color.Gray, shape = CircleShape)
            )
        }
    }
}
