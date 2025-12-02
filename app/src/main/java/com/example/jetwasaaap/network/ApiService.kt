package com.example.jetwasaaap.network

import com.example.jetwasaaap.data.MensajesDTO
import com.example.jetwasaaap.data.MensajesRequest
import com.example.jetwasaaap.data.UsuariosDTO
import com.example.jetwasaaap.data.UsuariosRequest
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @POST("login")
    suspend fun login(@Body request: UsuariosRequest): Response<UsuariosDTO>

    @GET("get-mensajes")
    suspend fun getMensajes(): Response<List<MensajesDTO>>

    @POST("create-mensaje")
    suspend fun sendMensaje(@Body request: MensajesRequest): Response<MensajesDTO>

    @GET("get-usuario/{id}")
    suspend fun getUsuario(@Path("id")id: Int): Response<UsuariosDTO>
}