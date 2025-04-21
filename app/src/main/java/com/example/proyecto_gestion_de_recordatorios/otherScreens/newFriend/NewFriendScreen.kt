package com.example.proyecto_gestion_de_recordatorios.otherScreens.newFriend

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyecto_gestion_de_recordatorios.ui.theme.background_newfriend
import com.example.proyecto_gestion_de_recordatorios.ui.theme.bar
import com.example.proyecto_gestion_de_recordatorios.ui.theme.button_login_newfriend

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview
@Composable
fun NewFriendScreen(
    onBack: () -> Unit = {},
    onAddFriend: (String) -> Unit = {}
) {
    var friendId by remember { mutableStateOf("") }

    Scaffold(
        containerColor = background_newfriend,
        bottomBar = {
            BottomAppBar(
                containerColor = bar,
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                IconButton(onClick = { onBack() }) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver",
                        tint = Color.Black
                    )
                }
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 60.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(220.dp))
            Text(
                text = "Añadir nuevo amigo",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                textDecoration = TextDecoration.Underline
            )

            Spacer(modifier = Modifier.height(30.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Id del usuario:",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(end = 8.dp)
                )

                TextField(
                    value = friendId,
                    onValueChange = { friendId = it },
                    singleLine = true,
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color(0xFFE7DEEB),
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent
                    ),
                    modifier = Modifier.width(180.dp)
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = { onAddFriend(friendId) },
                colors = ButtonDefaults.buttonColors(containerColor = button_login_newfriend),
                shape = RoundedCornerShape(50),
                modifier = Modifier.width(120.dp)
            ) {
                Text("Añadir", color = Color.White)
            }
        }
    }
}