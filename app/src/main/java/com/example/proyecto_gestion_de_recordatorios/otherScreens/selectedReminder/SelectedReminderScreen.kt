package com.example.proyecto_gestion_de_recordatorios.otherScreens.selectedReminder


import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.example.proyecto_gestion_de_recordatorios.ui.theme.reminder_compatir
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.Dialog
import coil.compose.rememberAsyncImagePainter
import com.example.proyecto_gestion_de_recordatorios.data.Recordatorio
import com.example.proyecto_gestion_de_recordatorios.ui.theme.selectedreminder_compartir

/**
 * Pantalla que muestra el recordatorio que selecionaste con más informacion
 */
@OptIn(ExperimentalMaterial3Api::class)
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
    val reminder by viewModel.selectedReminder.collectAsState()
    val profilePhotoUrl by viewModel.profilePhotoUrl.collectAsState()
    val amigos = viewModel.contactosAmigos
    val seleccionados = viewModel.amigosSeleccionados
    val context = LocalContext.current
    var showEditDialog by remember { mutableStateOf(false) }
    var showCompartirDialog by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }
    var recordatorioACompartir by remember { mutableStateOf<Recordatorio?>(null) }
    var nuevoTitulo by remember { mutableStateOf("") }
    var nuevaDescripcion by remember { mutableStateOf("") }
    val nombreUsuario = viewModel.nombreUsuarioActual
    LaunchedEffect(id_recordatorio) {
        viewModel.loadReminderById(id_recordatorio)
        viewModel.loadProfilePhoto()
        viewModel.obtenerContactos()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
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
                                DropdownMenuItem(text = { Text("Home") }, onClick = {
                                    expanded = false
                                    navegateToHome()
                                })
                                DropdownMenuItem(text = { Text("Amigos") }, onClick = {
                                    expanded = false
                                    navegateToFriend()
                                })
                                DropdownMenuItem(text = { Text("Categorías Creadas") }, onClick = {
                                    expanded = false
                                    navegateToCategory()
                                })
                            }
                        }
                        Text(
                            text = "Recordatorio seleccionado",
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 16.dp),
                            color = Color.Black,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        IconButton(onClick = { navegateToProfile() }) {
                            if (profilePhotoUrl != null) {
                                AsyncImage(
                                    model = profilePhotoUrl,
                                    contentDescription = "Foto de perfil",
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.AccountCircle,
                                    contentDescription = "Perfil",
                                    modifier = Modifier.size(40.dp),
                                    tint = Color.Black
                                )
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = bar)
            )
        },
        bottomBar = {
            BottomAppBar(containerColor = bar) {
                IconButton(onClick = { navegateToBack() }) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver",
                        tint = Color.Black
                    )
                }
            }
        },
        containerColor = background_rnrfc
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            reminder?.let {

                val colorRecordatorio = try {
                    Color(
                        android.graphics.Color.parseColor(
                            if (it.color.startsWith("#")) it.color else "#${it.color}"
                        )
                    )
                } catch (e: IllegalArgumentException) {
                    Color.Gray
                }
                val colorCategoria = try {
                    Color(
                        android.graphics.Color.parseColor(
                            if (it.color_de_la_categoria?.startsWith("#") == true) it.color_de_la_categoria else "#${it.color_de_la_categoria}"
                        )
                    )
                } catch (e: Exception) {
                    null
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = colorRecordatorio)
                ) {
                    Box(modifier = Modifier.fillMaxWidth()) {


                            colorCategoria?.let { it1 ->
                                Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(8.dp)
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(it1)
                            }?.let { it2 ->
                                Box(
                                    modifier = it2
                                )
                            }



                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = it.titulo ?: "",
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp
                            )
                            Spacer(Modifier.height(8.dp))
                            Text(text = it.descripcion ?: "", fontSize = 16.sp)
                            Spacer(Modifier.height(8.dp))
                            Text(text = "Fecha: ${it.fecha ?: ""}")
                            Text(text = "Prioridad: ${it.prioridad ?: ""}")


                            if (it.esta_Compartido == true && it.compartidoPor != nombreUsuario) {
                                it.compartidoPor?.let { nombre ->
                                    Spacer(Modifier.height(4.dp))
                                    Text(text = "Compartido por: $nombre", fontSize = 12.sp)
                                }
                            }

                            Spacer(Modifier.height(16.dp))


                            Row(horizontalArrangement = Arrangement.Center) {
                                Button(
                                    onClick = {
                                        recordatorioACompartir = reminder
                                        showCompartirDialog = true
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = selectedreminder_compartir)
                                ) {
                                    Text("Compartir")
                                }
                                Spacer(Modifier.width(140.dp))
                                if (reminder?.esEditable == true) {
                                    IconButton(
                                        onClick = {
                                            nuevoTitulo = reminder?.titulo ?: ""
                                            nuevaDescripcion = reminder?.descripcion ?: ""
                                            showEditDialog = true
                                        }
                                    ) {
                                        Icon(Icons.Default.Edit, contentDescription = "Editar")
                                    }
                                }
                            }
                        }
                    }
                }
            } ?: Text("Cargando...", fontSize = 16.sp)

            if (showEditDialog) {
                AlertDialog(
                    onDismissRequest = { showEditDialog = false },
                    confirmButton = {
                        TextButton(onClick = {
                            viewModel.editarTituloYDescripcion(
                                nuevoTitulo,
                                nuevaDescripcion,
                                id_recordatorio = id_recordatorio
                            )
                            showEditDialog = false
                        }) { Text("Guardar") }
                    },
                    dismissButton = {
                        TextButton(onClick = { showEditDialog = false }) { Text("Cancelar") }
                    },
                    title = { Text("Editar Recordatorio") },
                    text = {
                        Column {
                            OutlinedTextField(
                                value = nuevoTitulo,
                                onValueChange = { nuevoTitulo = it },
                                label = { Text("Título") }
                            )
                            OutlinedTextField(
                                value = nuevaDescripcion,
                                onValueChange = { nuevaDescripcion = it },
                                label = { Text("Descripción") }
                            )
                        }
                    }
                )
            }

            if (showCompartirDialog && recordatorioACompartir != null) {
                Dialog(onDismissRequest = {
                    showCompartirDialog = false
                    viewModel.limpiarSeleccionAmigos()
                }) {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color.White,
                        tonalElevation = 8.dp
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Selecciona amigos para compartir:", fontWeight = FontWeight.Bold)

                            LazyColumn(modifier = Modifier.height(300.dp)) {
                                items(amigos) { amigo ->
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(8.dp)
                                    ) {
                                        Image(
                                            painter = rememberAsyncImagePainter(amigo.imagenUrl),
                                            contentDescription = "Foto de perfil",
                                            modifier = Modifier
                                                .size(40.dp)
                                                .clip(CircleShape),
                                            contentScale = ContentScale.Crop
                                        )
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Text(text = amigo.nombre)
                                        Spacer(modifier = Modifier.weight(1f))
                                        Checkbox(
                                            checked = seleccionados.any { it.id == amigo.referencia.split("/").last() },
                                            onCheckedChange = {
                                                viewModel.toggleSeleccionAmigo(amigo.referencia)
                                            }
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                                TextButton(onClick = {
                                    showCompartirDialog = false
                                    viewModel.limpiarSeleccionAmigos()
                                }) {
                                    Text("Cancelar")
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Button(
                                    onClick = {
                                        recordatorioACompartir?.let { recordatorio ->
                                            viewModel.compartirRecordatorio(recordatorio, context) {
                                                Toast.makeText(context, "Recordatorio compartido correctamente", Toast.LENGTH_SHORT).show()
                                                showCompartirDialog = false
                                                viewModel.limpiarSeleccionAmigos()
                                            }
                                        }
                                    },
                                    enabled = seleccionados.isNotEmpty(),
                                    colors = ButtonDefaults.buttonColors(containerColor = reminder_compatir)
                                ) {
                                    Text("Compartir")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
