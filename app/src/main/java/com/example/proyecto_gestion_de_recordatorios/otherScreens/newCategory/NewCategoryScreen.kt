package com.example.proyecto_gestion_de_recordatorios.otherScreens.newCategory

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.BottomAppBar
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyecto_gestion_de_recordatorios.ui.theme.background_newcategory
import com.example.proyecto_gestion_de_recordatorios.ui.theme.background_rnrfc
import com.example.proyecto_gestion_de_recordatorios.ui.theme.bar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions

import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview
@Composable
fun NewCategoryScreen() {
    Scaffold(
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
        },
        containerColor = background_newcategory
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(background_newcategory)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Nueva Categoría",
                    textDecoration = TextDecoration.Underline,
                    fontSize = 22.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )

                // Campo de nombre
                OutlinedTextField(
                    value = "",
                    onValueChange = { /* Actualizar nombre */ },
                    label = { Text("Nombre") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth(0.8f)
                )

                // Slider de selección de color
                val colorOptions = listOf(
                    Color.Red,
                    Color.Blue,
                    Color.Green,
                    Color.Yellow,
                    Color.Magenta,
                    Color.Cyan,
                    Color.Black
                )
                var colorIndex by remember { mutableStateOf(0f) }

                Text("Color", fontSize = 16.sp, color = Color.Black)
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(50.dp)
                        .background(colorOptions[colorIndex.toInt()])
                        .border(1.dp, Color.Black)
                )

                Slider(
                    value = colorIndex,
                    onValueChange = { colorIndex = it },
                    valueRange = 0f..(colorOptions.size - 1).toFloat(),
                    steps = colorOptions.size - 2,
                    modifier = Modifier.fillMaxWidth(0.8f),
                    colors = SliderDefaults.colors(
                        thumbColor = colorOptions[colorIndex.toInt()],
                        activeTrackColor = colorOptions[colorIndex.toInt()],
                        inactiveTrackColor = Color.LightGray
                    )
                )

                // Botones
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = { /* Acción de crear */ },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFB396))
                    ) {
                        Text("Crear", color = Color.Black)
                    }

                    Button(
                        onClick = { /* Acción de cancelar */ },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                    ) {
                        Text("Cancelar", color = Color.Black)
                    }
                }
            }
        }
    }
}