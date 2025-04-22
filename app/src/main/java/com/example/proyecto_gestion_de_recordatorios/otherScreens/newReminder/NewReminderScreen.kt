package com.example.proyecto_gestion_de_recordatorios.otherScreens.newReminder

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape
import com.example.proyecto_gestion_de_recordatorios.ui.theme.background_newreminder
import com.example.proyecto_gestion_de_recordatorios.ui.theme.button_cancel
import com.example.proyecto_gestion_de_recordatorios.ui.theme.default_button_color
import com.example.proyecto_gestion_de_recordatorios.ui.theme.newreminder_select_category


@Composable
fun NewReminderScreen(navegateToReminder: () -> Unit) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }
    var colorIndex by remember { mutableStateOf(0f) }
    var priority by remember { mutableStateOf(0f) }
    var isEditable by remember { mutableStateOf(false) }

    val colorOptions = listOf(
        Color.Red, Color.Blue, Color.Green, Color.Yellow, Color.Magenta,
        Color.Cyan, Color.Gray, Color.Black, Color(0xFF65558F), Color(0xFFF39AFF)
    )

    val priorityLevels = listOf("Baja", "Media", "Alta")

    Scaffold(
        containerColor = background_newreminder
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(40.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Nuevo  Recordatorio",
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                textDecoration = TextDecoration.Underline
            )

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Título") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
                value = time,
                onValueChange = { time = it },
                label = { Text("Hora de recuerdo") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Color", fontWeight = FontWeight.SemiBold)
                Slider(
                    value = colorIndex,
                    onValueChange = { colorIndex = it },
                    valueRange = 0f..(colorOptions.lastIndex).toFloat(),
                    steps = colorOptions.size - 2,
                    colors = SliderDefaults.colors(
                        thumbColor = colorOptions[colorIndex.toInt()],
                        activeTrackColor = colorOptions[colorIndex.toInt()]
                    ),
                    modifier = Modifier
                        .size(220.dp, 90.dp)
                        .padding(start = 40.dp)
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Prioridad", fontWeight = FontWeight.SemiBold)
                Slider(
                    value = priority,
                    onValueChange = { priority = it },
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
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ){
            Text(text = "Asignar Categoría", fontWeight = FontWeight.SemiBold)
            Button(
                onClick = { /* abrir selección de categorías */ },
                colors = ButtonDefaults.buttonColors(containerColor = newreminder_select_category),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text("Categorías existentes", color = Color.White)
            }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("¿Es editable por otros usuarios?")
                Spacer(Modifier.width(40.dp))
                Switch(
                    checked = isEditable,
                    onCheckedChange = { isEditable = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = default_button_color
                    )
                )
            }

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(containerColor = default_button_color),
                    shape = RoundedCornerShape(50)
                ) {
                    Text("Crear", color = Color.White)
                }
                Button(
                    onClick = {  },
                    colors = ButtonDefaults.buttonColors(containerColor = button_cancel),
                    shape = RoundedCornerShape(50)
                ) {
                    Text("Cancelar", color = Color.Black)
                }
            }
        }
    }
}