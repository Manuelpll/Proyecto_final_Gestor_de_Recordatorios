package com.example.proyecto_gestion_de_recordatorios.otherScreens.newReminder

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.proyecto_gestion_de_recordatorios.ui.theme.background_newreminder
import com.example.proyecto_gestion_de_recordatorios.ui.theme.button_cancel
import com.example.proyecto_gestion_de_recordatorios.ui.theme.default_button_color
import com.example.proyecto_gestion_de_recordatorios.ui.theme.newreminder_select_category

/**
 * Pantalla para crear un nuevo recordatorio
 */
@Composable
fun NewReminderScreen(navegateToReminder: () -> Unit, viewModel: NewReminderViewModel = hiltViewModel()) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var expanded by remember { mutableStateOf(false) }

    // Cargar categorías al entrar
    LaunchedEffect(Unit) {
        viewModel.cargarCategorias()
    }

    Scaffold(containerColor = background_newreminder) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(40.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Nuevo Recordatorio", fontWeight = FontWeight.Bold, fontSize = 22.sp, textDecoration = TextDecoration.Underline)

            OutlinedTextField(
                value = viewModel.titulo,
                onValueChange = { viewModel.titulo = it },
                label = { Text("Título") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
                value = viewModel.descripcion,
                onValueChange = { viewModel.descripcion = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
                value = viewModel.fechaHora,
                onValueChange = { viewModel.fechaHora = it },
                label = { Text("Hora de recuerdo( Sigue el ejemplo)") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            // Selector de color
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Text("Color", fontWeight = FontWeight.SemiBold)
                Slider(
                    value = viewModel.colorIndex,
                    onValueChange = { viewModel.colorIndex = it },
                    valueRange = 0f..(viewModel.colores.lastIndex).toFloat(),
                    steps = viewModel.colores.size - 2,
                    colors = SliderDefaults.colors(
                        thumbColor = viewModel.colores[viewModel.colorIndex.toInt()],
                        activeTrackColor = viewModel.colores[viewModel.colorIndex.toInt()]
                    ),
                    modifier = Modifier
                        .size(220.dp, 90.dp)
                        .padding(start = 40.dp)
                )
            }

            // Selector de prioridad
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Text("Prioridad", fontWeight = FontWeight.SemiBold)
                Slider(
                    value = viewModel.prioridadIndex,
                    onValueChange = { viewModel.prioridadIndex = it },
                    valueRange = 0f..2f,
                    steps = 1,
                    colors = SliderDefaults.colors(
                        thumbColor = default_button_color,
                        activeTrackColor = default_button_color
                    ),
                    modifier = Modifier
                        .size(220.dp, 90.dp)
                        .padding(start = 40.dp)
                )
            }

            // Dropdown de categorías
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Asignar Categoría", fontWeight = FontWeight.SemiBold)
                Box {
                    Button(
                        onClick = { expanded = true },
                        colors = ButtonDefaults.buttonColors(containerColor = newreminder_select_category),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text("Categorías existentes", color = Color.White)
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        viewModel.categoriasDisponibles
                            .filterKeys { it.isNotBlank() } //  filtramos claves vacías
                            .forEach { (nombre, color) ->
                                DropdownMenuItem(
                                    text = {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Box(
                                                modifier = Modifier
                                                    .size(12.dp)
                                                    .background(color, shape = CircleShape)
                                            )
                                            Spacer(Modifier.width(8.dp))
                                            Text(nombre)
                                        }
                                    },
                                    onClick = {
                                        viewModel.categoriaSeleccionada = nombre
                                        expanded = false
                                    }
                                )
                         }
                    }
                }
            }

            // Switch editable
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("¿Es editable por otros usuarios?")
                Spacer(Modifier.width(40.dp))
                Switch(
                    checked = viewModel.esEditable,
                    onCheckedChange = { viewModel.esEditable = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = default_button_color
                    )
                )
            }

            // Botones
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        viewModel.guardarRecordatorio(
                            onSuccess = { navegateToReminder() },
                            onFailure = { Toast.makeText(context, "Error al guardar el recordatorio", Toast.LENGTH_SHORT).show()}
                        )
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = default_button_color),
                    shape = RoundedCornerShape(50)
                ) {
                    Text("Crear", color = Color.White)
                }
                Button(
                    onClick = { navegateToReminder() },
                    colors = ButtonDefaults.buttonColors(containerColor = button_cancel),
                    shape = RoundedCornerShape(50)
                ) {
                    Text("Cancelar", color = Color.Black)
                }
            }
        }
    }
}
