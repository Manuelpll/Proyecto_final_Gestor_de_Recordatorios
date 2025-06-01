package com.example.proyecto_gestion_de_recordatorios.otherScreens.newCategory

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyecto_gestion_de_recordatorios.ui.theme.background_newcategory
import com.example.proyecto_gestion_de_recordatorios.ui.theme.bar
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.proyecto_gestion_de_recordatorios.ui.theme.button_newcategory

/**
 * Pantalla que aparece para crear una nueva categoria
 */
@Composable
fun NewCategoryScreen(
    viewModel: NewCategoryViewModel = hiltViewModel(),
    navigateToBack: () -> Boolean,
    navegateToCategory: () -> Unit
) {
    val nombre = viewModel.nombre
    val colorIndex = viewModel.colorIndex
    val colores = viewModel.colores

    Scaffold(
        bottomBar = {
            BottomAppBar(
                containerColor = bar,
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                IconButton(onClick = { navigateToBack() }) {
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
                Spacer(modifier = Modifier.height(140.dp))
                Text(
                    text = "Nueva Categoría",
                    textDecoration = TextDecoration.Underline,
                    fontSize = 22.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )

                OutlinedTextField(
                    value = nombre,
                    onValueChange = { viewModel.onNombreChange(it) },
                    label = { Text("Nombre") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth(0.8f)
                )

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Color", fontSize = 16.sp, color = Color.Black)
                    Spacer(modifier = Modifier.padding(start = 30.dp))
                    Slider(
                        value = colorIndex,
                        onValueChange = { viewModel.onColorIndexChange(it) },
                        valueRange = 0f..(colores.size - 1).toFloat(),
                        steps = colores.size - 2,
                        modifier = Modifier.fillMaxWidth(0.6f),
                        colors = SliderDefaults.colors(
                            thumbColor = colores[colorIndex.toInt()],
                            activeTrackColor = colores[colorIndex.toInt()],
                            inactiveTrackColor = Color.LightGray
                        )
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = {
                            viewModel.crearCategoria {
                                navegateToCategory()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = button_newcategory)
                    ) {
                        Text("Crear", color = Color.Black)
                    }

                    Button(
                        onClick = { navigateToBack() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                    ) {
                        Text("Cancelar", color = Color.Black)
                    }
                }
            }
        }
    }
}