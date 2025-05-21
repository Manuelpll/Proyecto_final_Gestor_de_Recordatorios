package com.example.proyecto_gestion_de_recordatorios.otherScreens.selectedReminder

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.proyecto_gestion_de_recordatorios.ui.theme.background_rnrfc
import com.example.proyecto_gestion_de_recordatorios.ui.theme.bar
import androidx.compose.runtime.collectAsState
import com.example.proyecto_gestion_de_recordatorios.ui.theme.selectedreminder_compartir

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SelectedReminderScreen(
    navegateToProfile: () -> Unit,
    navegateToCategory: () -> Unit,
    navegateToFriend: () -> Unit,
    navegateToHome: () -> Unit,
    id_recordatorio: String,
    viewModel: SelectedReminderViewModel = hiltViewModel(),
    navegateToBack: () -> Boolean
) {
    val recordatorio by viewModel.selectedReminder.collectAsState()
    val profilePhotoUrl by viewModel.profilePhotoUrl.collectAsState()

    // Cargar datos al entrar en la pantalla
    LaunchedEffect(id_recordatorio) {
        viewModel.loadReminderById(id_recordatorio)
        viewModel.loadProfilePhoto()
    }
    val categoriaColor = recordatorio?.color_de_la_categoria?.toColorOrDefault() ?: Color.Gray
    var expanded by remember { mutableStateOf(false) }

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
                                    onClick = { expanded = false; navegateToHome() }
                                )
                                DropdownMenuItem(
                                    text = { Text("Amigos") },
                                    onClick = { expanded = false; navegateToFriend() }
                                )
                                DropdownMenuItem(
                                    text = { Text("Categorías Creadas") },
                                    onClick = { expanded = false; navegateToCategory() }
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
                        IconButton(
                            onClick = { navegateToProfile() },
                            modifier = Modifier.size(70.dp)
                        ) {
                            if (profilePhotoUrl != null) {
                                AsyncImage(
                                    model = profilePhotoUrl,
                                    contentDescription = "Foto de perfil",
                                    modifier = Modifier
                                        .size(50.dp)
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                Icon(
                                    Icons.Default.AccountCircle,
                                    contentDescription = "Perfil",
                                    modifier = Modifier.size(70.dp).padding(end = 20.dp),
                                    tint = Color.Black
                                )
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = bar)
            )
        },
        containerColor = background_rnrfc,
        bottomBar = {
            BottomAppBar(containerColor = bar, contentPadding = PaddingValues(horizontal = 16.dp)) {
                IconButton(onClick = { navegateToBack() }) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver",
                        tint = Color.Black
                    )
                }
            }
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            recordatorio?.let { reminder->
                val colorReminder= reminder.color.toColorOrDefault() ?: Color.LightGray
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(colorReminder)
                        .padding(20.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.End)
                                .size(20.dp)
                                .background(color = categoriaColor)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = reminder.titulo ?: "Sin título",
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = reminder.descripcion ?: "Sin descripción",
                            color = Color.Red,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Fecha en la que está:", fontWeight = FontWeight.SemiBold)
                            Text(reminder.fecha ?: "Sin fecha")
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Prioridad:", fontWeight = FontWeight.SemiBold)
                            Text(
                                "~${reminder.prioridad}"
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Button(
                            onClick = { /* Acción compartir */ },
                            colors = ButtonDefaults.buttonColors(containerColor = selectedreminder_compartir),
                            shape = RoundedCornerShape(30.dp)
                        ) {
                            Text("Compartir", color = Color.White)
                        }

                        if (reminder.esta_Compartido == false) {
                            Spacer(modifier = Modifier.height(15.dp))
                            Text(
                                "No tienes permiso para editarlo",
                                fontSize = 12.sp,
                                color = Color.DarkGray
                            )
                        } else {

                            reminder.compartidoPor?.let {
                                Spacer(modifier = Modifier.height(5.dp))
                                Text(
                                    text = "Compartido por $it",
                                    fontSize = 12.sp,
                                    color = Color.DarkGray
                                )
                            }
                        }
                    }
                }
            } ?: run {
                Text(text = "Cargando recordatorio...", fontSize = 16.sp)
            }
        }
    }
    }
fun String.toColorOrDefault(default: Color = Color.Gray): Color {
    return try {
        Color(android.graphics.Color.parseColor(this))
    } catch (e: IllegalArgumentException) {
        default
    }
}