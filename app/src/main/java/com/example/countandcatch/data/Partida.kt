package com.example.countandcatch.data

import java.io.Serializable

data class Partida(
    val nombre: String,
    val edad: Int,
    val tiempo_partida: Int,
    val errores: Int,
    val puntos: Int,
    val fecha_hora: String,
    val juego: Int, //1=count 2=catch
    val dificultad: Int, //1=baja 2=media 3=alta
    val terminada: Int //0=no 1=si
): Serializable