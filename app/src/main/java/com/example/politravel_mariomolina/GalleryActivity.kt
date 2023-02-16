package com.example.politravel_mariomolina

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.GridView

class GalleryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)


        val intent = getIntent()
        val activity = intent.getStringExtra(MainActivity.paquetConstants.ADD_TO_IMG) as String
        val names = arrayOf("prueba2.jpg","peru.jpg","islasgriegas.jpg","prueba.jpg","tahiti.jpg","paris.jpg")

        val grid = findViewById<GridView>(R.id.gridGallery)
        val customAdapter = GalleryAdapter(this, R.layout.gallery_item,names)

        grid.adapter = customAdapter

        grid.onItemClickListener = AdapterView.OnItemClickListener()
        {
            _,_,i,_ ->
             val intent = Intent(this,AddActivity::class.java)
             intent.putExtra(MainActivity.paquetConstants.IMG,names[i])
             intent.putExtra(MainActivity.paquetConstants.IMAGE_BUTTON,activity)
             setResult(RESULT_OK,intent)
             finish()
        }
    }
}