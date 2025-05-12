package com.example.proyecto_gestion_de_recordatorios.navegation

import kotlinx.serialization.Serializable

@Serializable
object InitialScreen

@Serializable
object RegisterScreen

@Serializable
object LoginScreen

@Serializable
object HomeScreen

@Serializable
object ProfileScreen

@Serializable
object ReminderScreen

@Serializable
object NewReminderScreen

@Serializable
data class SelectedReminderScreen(val id_recordatorio:String)

@Serializable
object CategoryScreen

@Serializable
object NewCategoryScreen

@Serializable
object FriendScreen

@Serializable
object NewFriendScreen