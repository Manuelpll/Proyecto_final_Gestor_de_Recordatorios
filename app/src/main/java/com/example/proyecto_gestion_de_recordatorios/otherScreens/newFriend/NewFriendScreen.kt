package com.example.proyecto_gestion_de_recordatorios.otherScreens.newFriend

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.proyecto_gestion_de_recordatorios.data.Usuario
import com.example.proyecto_gestion_de_recordatorios.ui.theme.background_newfriend
import com.example.proyecto_gestion_de_recordatorios.ui.theme.bar
import com.example.proyecto_gestion_de_recordatorios.ui.theme.button_login_newfriend

/**
 * Pantalla que aparece al añadir a un nuevo amigo
 */
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NewFriendScreen(
    viewModel: NewFriendViewModel = hiltViewModel(),
    navegateToFriend: () -> Unit,
    navegateToBack: () -> Boolean
) {
    val context = LocalContext.current

    Scaffold(
        bottomBar = {
            BottomAppBar(containerColor = bar) {
                IconButton(onClick = { navegateToBack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = Color.Black)
                }
            }
        },
        containerColor = background_newfriend
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Añadir nuevo amigo",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = Color.Black,
                textDecoration = TextDecoration.Underline
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = viewModel.busqueda,
                onValueChange = {
                    viewModel.busqueda = it
                    viewModel.buscarUsuariosPorNombre()
                },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                placeholder = { Text("Buscar amigo") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(0.9f),
                colors = TextFieldDefaults.outlinedTextFieldColors(containerColor = Color.White)
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(viewModel.listaUsuarios) { usuario ->
                    AmigoItem(
                        usuario = usuario,
                        seleccionado = viewModel.usuarioSeleccionado?.uid == usuario.uid,
                        onSelect = { viewModel.seleccionarUsuario(usuario) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    viewModel.añadirContacto(
                        onSuccess = {
                            Toast.makeText(context, "Contacto añadido correctamente", Toast.LENGTH_SHORT).show()
                            navegateToFriend()
                        },
                        onFailure = {
                            Toast.makeText(context, "Error: $it", Toast.LENGTH_SHORT).show()
                        }
                    )
                },
                enabled = viewModel.usuarioSeleccionado != null,
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = button_login_newfriend,
                    disabledContainerColor = button_login_newfriend.copy(alpha = 0.5f)
                )
            ) {
                Text("Añadir", color = Color.White)
            }
        }
    }
}
@Composable
fun AmigoItem(
    usuario: Usuario,
    seleccionado: Boolean,
    onSelect: () -> Unit
) {
    val background = if (seleccionado) background_newfriend else Color(0xFFE0DEDE)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .background(background, shape = RoundedCornerShape(8.dp))
            .clickable { onSelect() }
            .padding(12.dp)
    ) {
        if(usuario.foto_perfil!=null) {
            AsyncImage(
                model = usuario.foto_perfil,
                contentDescription = "Foto de perfil",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
            )
        }else{
            Icon(
                Icons.Default.AccountCircle,
                contentDescription = "Foto de perfil",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape),
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(usuario.nombre, fontSize = 18.sp, color = Color.Black)
    }
}