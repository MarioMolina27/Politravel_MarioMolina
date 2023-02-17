package com.example.politravel_mariomolina.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.GridView
import com.example.politravel_mariomolina.adapters.GalleryAdapter
import com.example.politravel_mariomolina.datamodel.Keys
import com.example.politravel_mariomolina.R

class GalleryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        /**
         * Rebrem per intent el botò desde el qual hem cridat a l'activity per tal que quan retornem
         * la imatge sabrem a quin botó hem de col·locar la imatge
         * */
        val intent = getIntent()
        val activity = intent.getStringExtra(Keys.paquetConstants.ADD_TO_IMG) as String
        val names = arrayOf("prueba2.jpg","peru.jpg","islasgriegas.jpg","prueba.jpg","tahiti.jpg","paris.jpg")

        val grid = findViewById<GridView>(R.id.gridGallery)
        val customAdapter = GalleryAdapter(this, R.layout.gallery_item,names)

        grid.adapter = customAdapter

        grid.onItemClickListener = AdapterView.OnItemClickListener()
        {
            _,_,i,_ ->
             val intent = Intent(this, AddActivity::class.java)
             intent.putExtra(Keys.paquetConstants.IMG,names[i])
             intent.putExtra(Keys.paquetConstants.IMAGE_BUTTON,activity)
             setResult(RESULT_OK,intent)
             finish()
        }
    }
}