package com.example.politravel_mariomolina

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Spinner

class AddActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mod_add_layout)

        val transport = mutableListOf<Transport>(
            Transport("Avió",R.drawable.plane),
            Transport("Bus",R.drawable.bus),
            Transport("Tren",R.drawable.train))


        val lstTransport = findViewById<Spinner>(R.id.lstTransport)
        val adapter = TransportAdapter(this,R.layout.transport_item,transport)
        lstTransport.adapter=adapter

        adapter.setDropDownViewResource(R.layout.transport_item)


    }
}