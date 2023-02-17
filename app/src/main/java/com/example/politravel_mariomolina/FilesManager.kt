package com.example.politravel_mariomolina

import android.content.Context
import com.example.politravel_mariomolina.datamodel.Paquet
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.FileReader
import java.io.FileWriter

class FilesManager {
    companion object{

        fun getPaquets(context: Context): MutableList<Paquet>{

            val jsonFilePath = context.getFilesDir().toString() + "/json/paquets.json"
            val jsonFile = FileReader(jsonFilePath)
            val listPaquetsType = object: TypeToken<MutableList<Paquet>>() {}.type
            val planets: MutableList<Paquet> = Gson().fromJson(jsonFile, listPaquetsType)
            return planets
        }

        fun savePaquets(context: Context,planets: List<Paquet>){
            val jsonFilePath = context.getFilesDir().toString() + "/json/paquets.json"
            val jsonFile = FileWriter(jsonFilePath)
            var gson = Gson()
            var jsonElement = gson.toJson(planets)
            jsonFile.write(jsonElement)
            jsonFile.close()
        }
    }
}