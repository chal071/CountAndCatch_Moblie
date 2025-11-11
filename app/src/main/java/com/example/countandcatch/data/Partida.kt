package com.example.countandcatch.data

data class Partida(
    val nombre: String,
    val tiempo_partida: Int,
    val errores: Int,
    val puntuacion: Int,
    val fecha: String,
    val juego: Int,
    val dificultad: Int
)