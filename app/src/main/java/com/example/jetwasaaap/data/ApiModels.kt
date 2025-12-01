package com.example.jetwasaaap.data

import java.sql.Time
import java.sql.Timestamp

data class UsuariosDTO (
    val id: Int,
    val username: String,
    val password: String
)

data class MensajesDTO (
    val id: Int,
    val mensaje: String,
    val usuario: Int,
)

data class UsuariosRequest (
    val username: String,
    val password: String
)

data class MensajesRequest (
    val mensaje: String,
    val usuario: Int,
)