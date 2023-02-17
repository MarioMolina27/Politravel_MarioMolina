package com.example.politravel_mariomolina.datamodel

import java.io.Serializable

class Paquet(
    val idPaquet: Int,
    val nomPaquet: String,
    val descripcio: String, val imgLlista: String,
    val imgDetail: String,
    val transport:String, val iniciRecorregut: String, val longitud: Double, val latitud: Double, val grausGoogleMaps: Double,
    val finalRecorregut: String,
    val dies: Int,
    val itinerari: MutableList<Itinerari>):
    Serializable {

    constructor() : this(
        0,
        "",
        "",
        "",
        "",
        "",
        "",
        0.0,
        0.0,
        0.0,
        "",
        0,
        mutableListOf<Itinerari>()
    )
}