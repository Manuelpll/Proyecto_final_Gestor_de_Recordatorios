package com.example.proyecto_gestion_de_recordatorios.otherScreens.newFriend

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.proyecto_gestion_de_recordatorios.data.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NewFriendViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : ViewModel() {

    var busqueda by mutableStateOf("")
    var listaUsuarios by mutableStateOf<List<Usuario>>(emptyList())
        private set

    var usuarioSeleccionado by mutableStateOf<Usuario?>(null)

    fun buscarUsuariosPorNombre() {
        if (busqueda.isBlank()) {
            listaUsuarios = emptyList()
            return
        }

        firestore.collection("Users")
            .orderBy("nombre")
            .startAt(busqueda)
            .endAt(busqueda + "\uf8ff")
            .get()
            .addOnSuccessListener { result ->
                val actualUid = auth.currentUser?.uid
                listaUsuarios = result.documents.mapNotNull { doc ->
                    val uid = doc.id
                    if (uid != actualUid) {
                        Usuario(
                            uid = uid,
                            nombre = doc.getString("nombre") ?: "",
                            email = doc.getString("email") ?: "",
                            foto_perfil = doc.getString("FotosPerfil") ?: ""
                        )
                    } else null
                }
            }
    }

    fun seleccionarUsuario(usuario: Usuario) {
        usuarioSeleccionado = usuario
    }

    fun añadirContacto(
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        val actualUid = auth.currentUser?.uid
        val amigo = usuarioSeleccionado

        if (actualUid == null) {
            onFailure("Usuario no autenticado")
            return
        }

        val userRef = firestore.collection("Users").document(actualUid)
        val amigoRef = amigo?.let { it.uid?.let { it1 -> firestore.collection("Users").document(it1) } }

        firestore.runBatch { batch ->
            if (amigo != null) {
                batch.update(userRef, "contactos", FieldValue.arrayUnion(amigo.uid))
            }
            if (amigoRef != null) {
                batch.update(amigoRef, "contactos", FieldValue.arrayUnion(actualUid))
            }
        }.addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e.message ?: "Error al añadir contacto") }
    }
}