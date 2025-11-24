package com.example.countandcatch.data

import java.io.Serializable

data class Partida(
    val nombre: String,
    val tiempo_partida: Int,
    val puntos_o_errores: Int,
    val fecha: String,
    val juego: Int,
    val dificultad: Int
): Serializable