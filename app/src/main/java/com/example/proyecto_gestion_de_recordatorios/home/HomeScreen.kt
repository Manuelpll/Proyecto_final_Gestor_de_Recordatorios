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
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import com.example.proyecto_gestion_de_recordatorios.data.Recordatorio
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale
import androidx.hilt.navigation.compose.hiltViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navegateToReminder: () -> Unit,
    navegateToFriends: () -> Unit,
    navegateToCategory: () -> Unit,
    navegateToSelectedReminder: (String) -> Unit,
    navegateToProfile: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val recordatorios by remember { derivedStateOf { viewModel.recordatorios } }
    val profileImageUrl by viewModel.profileImageUrl

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
                                Icon(Icons.Default.Menu, contentDescription = "Menú", tint = Color.Black)
                            }
                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Mis Recordatorios") },
                                    onClick = {
                                        expanded = false
                                        navegateToReminder()
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Amigos") },
                                    onClick = {
                                        expanded = false
                                        navegateToFriends()
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Categorías Creadas") },
                                    onClick = {
                                        expanded = false
                                        navegateToCategory()
                                    }
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
                        IconButton(
                            onClick = { navegateToProfile() },
                            modifier = Modifier
                                .size(70.dp)
                        ) {
                            if (profileImageUrl != null) {
                                AsyncImage(
                                    model = profileImageUrl,
                                    contentDescription = "Perfil",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(50.dp)
                                        .clip(CircleShape)
                                        .border(2.dp, Color.Black, CircleShape)
                                )
                            } else {
                                Icon(
                                    Icons.Default.AccountCircle,
                                    contentDescription = "Perfil",
                                    modifier = Modifier
                                        .size(70.dp)
                                        .padding(end = 20.dp),
                                    tint = Color.Black
                                )
                            }
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
            items(recordatorios) { reminder ->
                ReminderCard(reminder) { reminder.id?.let { navegateToSelectedReminder(it) } }
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun ReminderCard(recordatorio: Recordatorio,navegateToSelectedReminder: () -> Unit) {
    val colorRecordatorio = try {
        Color(
            android.graphics.Color.parseColor(
                if (recordatorio.color.startsWith("#")) recordatorio.color else "#${recordatorio.color}"
            )
        )
    } catch (e: IllegalArgumentException) {
        Color.Gray
    }

    val colorCategoria = try {
        Color(
            android.graphics.Color.parseColor(
                if (recordatorio.color_de_la_categoria?.startsWith("#") == true) recordatorio.color_de_la_categoria else "#${recordatorio.color_de_la_categoria}"
            )
        )
    } catch (e: Exception) {
        null
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxSize(),
            colors = CardDefaults.cardColors( containerColor =  colorRecordatorio
            ),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            onClick = {navegateToSelectedReminder()}
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
       colorCategoria?.let {
            Modifier
                .align(Alignment.TopEnd)
                .padding(4.dp)
                .size(16.dp)
                .clip(CircleShape)
                .background(color = it)
        }?.let {
            Box(
                modifier = it
            )
        }
    }
}