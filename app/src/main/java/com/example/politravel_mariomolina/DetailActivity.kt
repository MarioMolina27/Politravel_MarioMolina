package com.example.politravel_mariomolina

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView


class DetailActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val intent = getIntent()
        val paquet = intent.getSerializableExtra(MainActivity.paquetConstants.PAQUET) as Paquet
        val imgDetailTrip = findViewById<ImageView>(R.id.imgDetailTrip)
        val imgPath = getFilesDir().toString()+ "/img/"+paquet.imgDetail
        val bitmap = BitmapFactory.decodeFile(imgPath)
        imgDetailTrip.setImageBitmap(bitmap)

        val txtTripNameDetail = findViewById<TextView>(R.id.txtTripNameDetail)
        val tempsDetail = findViewById<TextView>(R.id.tempsDetail)
        val imgTransport =  findViewById<ImageView>(R.id.imgTransport)
        val txtIniciViatje= findViewById<TextView>(R.id.txtIniciViatje)
        val txtFinalViatje= findViewById<TextView>(R.id.txtFinalViatje)
        val txtDescripcioDetail =  findViewById<TextView>(R.id.txtDescripcioDetail)
        val lstItinerari =  findViewById<RecyclerView>(R.id.lstItinerari)


        txtTripNameDetail.text = paquet.nomPaquet
        tempsDetail.text = paquet.dies.toString() + " dies"
        txtIniciViatje.text = paquet.iniciRecorregut
        txtFinalViatje.text = paquet.finalRecorregut
        txtDescripcioDetail.text = paquet.descripcio

        var adapter = ItinerariAdapter(this,paquet.itinerari)
        lstItinerari.hasFixedSize()
        lstItinerari.layoutManager = LinearLayoutManager(this)
        lstItinerari.adapter=adapter

        if(paquet.transport.equals("Avio"))
        {
            imgTransport.setImageResource(R.drawable.plane)
        }
        else if(paquet.transport.equals("Bus"))
        {
            imgTransport.setImageResource(R.drawable.bus)
        }
        else if(paquet.transport.equals("Tren"))
        {
            imgTransport.setImageResource(R.drawable.train)
        }

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val newWidth = displayMetrics.widthPixels
        val defaultLineWidth: Int = 87
        val defaultWidthScreen = 1080
        val newWidthLine = defaultLineWidth*newWidth/defaultWidthScreen
        val line1 = findViewById<View>(R.id.line1Detail)
        val line2 = findViewById<View>(R.id.line2Detail)
        changeViewDimensions(line1,newWidthLine,4)
        changeViewDimensions(line2,newWidthLine,4)

        val secondFragment = supportFragmentManager.findFragmentById(R.id.FgrMaps) as MapsFragment?
        secondFragment?.recivePaquet(paquet)
    }

    fun changeViewDimensions(view: View, widthDp: Int, heightDp: Int) {
        val scale = view.context.resources.displayMetrics.density
        view.layoutParams.width = (widthDp * scale + 0.5f).toInt()
        view.layoutParams.height = (heightDp * scale + 0.5f).toInt()
    }

}