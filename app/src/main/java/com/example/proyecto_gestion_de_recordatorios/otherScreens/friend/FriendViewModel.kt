package com.example.proyecto_gestion_de_recordatorios.otherScreens.friend

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.proyecto_gestion_de_recordatorios.data.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FriendViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _listaAmigos = mutableStateListOf<Usuario>()
    val listaAmigos: List<Usuario> = _listaAmigos

    private val _fotoPerfil = mutableStateOf<String?>(null)
    val fotoPerfil: State<String?> = _fotoPerfil

    init {
        cargarAmigos()
        cargarFotoPerfil()
    }

    private fun cargarAmigos() {
        val uid = auth.currentUser?.uid

        if (uid != null) {
            firestore.collection("Users").document(uid)
                .get()
                .addOnSuccessListener { document ->
                    val listaUids = document.get("contactos") as? List<String> ?: emptyList()
                    for (amigoUid in listaUids) {
                        firestore.collection("Users").document(amigoUid)
                            .get()
                            .addOnSuccessListener { amigoDoc ->
                                val nombre = amigoDoc.getString("nombre") ?: "Nombre desconocido"
                                val amigo = Usuario(
                                    uid = amigoUid,
                                    nombre = nombre,
                                    foto_perfil = amigoDoc.getString("foto_perfil_url")
                                )
                                _listaAmigos.add(amigo)
                            }
                    }
                }
        }
    }

    private fun cargarFotoPerfil() {
        val uid = auth.currentUser?.uid ?: return
        val ref = storage.reference.child("imagenes_perfil/$uid.jpg")
        ref.downloadUrl.addOnSuccessListener { uri ->
            _fotoPerfil.value = uri.toString()
        }
    }
}