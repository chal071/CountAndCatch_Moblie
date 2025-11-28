package com.example.countandcatch.data

import java.io.Serializable

data class Partida(
    val nombre: String,
    val edad: Int,
    val tiempo_partida: Int,
    val errores: Int,
    val puntos: Int,
    val fecha: String,
    val juego: Int,
    val dificultad: Int
): Serializable