package com.example.proyecto_gestion_de_recordatorios.otherScreens.newFriend

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyecto_gestion_de_recordatorios.data.Usuario
import com.example.proyecto_gestion_de_recordatorios.ui.theme.background_newfriend
import com.example.proyecto_gestion_de_recordatorios.ui.theme.bar
import com.example.proyecto_gestion_de_recordatorios.ui.theme.button_login_newfriend

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview
@Composable
fun NewFriendScreen(
    onAddFriend: (String) -> Unit = {}
) {
    var selectedFriend by remember { mutableStateOf<String?>(null) }

    val friendsList = listOf(
        Usuario(nombre = "Javier", email = "javier@example.com"),
        Usuario(nombre = "Mar", email = "mar@example.com"),
        Usuario(nombre = "Kevin", email = "kevin@example.com"),
        Usuario(nombre = "Mireya", email = "mireya@example.com")
    )

    Scaffold(
        bottomBar = {
            BottomAppBar(
                containerColor = bar,
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                IconButton(onClick = { /* Acción volver */ }) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver",
                        tint = Color.Black
                    )
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
                value = "",
                onValueChange = {},
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                placeholder = { Text("Buscar amigo") },
                singleLine = true,
                enabled = false,
                modifier = Modifier.fillMaxWidth(0.9f),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(friendsList) { friend ->
                    Amigo(
                        friend = friend,
                        onSelect = { selectedFriend = it }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { selectedFriend?.let { onAddFriend(it) }  },
                colors = ButtonDefaults.buttonColors(
                containerColor = button_login_newfriend,
                    disabledContainerColor = button_login_newfriend.copy(alpha = 0.5f)),
                enabled = selectedFriend != null,
                shape = RoundedCornerShape(50)
            ) {
                Text("Añadir", color = Color.White)
            }
        }
    }
}

@Composable
private fun Amigo(
    friend: Usuario,
    onSelect: (String) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .background(Color(0xFFE0DEDE), shape = RoundedCornerShape(8.dp))
            .clickable { onSelect(friend.nombre) }
            .padding(12.dp)
    ) {
        Icon(
            Icons.Default.AccountCircle,
            contentDescription = null,
            tint = Color.Black,
            modifier = Modifier.size(32.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = friend.nombre,
            fontSize = 18.sp,
            color = Color.Black
        )
    }
}