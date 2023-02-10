package com.example.politravel_mariomolina

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

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

        val edtNomPaquet = findViewById<EditText>(R.id.edtxtNomPaquet)
        val edtDuracio = findViewById<EditText>(R.id.edtxtDuracio)
        val edtDescripcio = findViewById<EditText>(R.id.edtxtDescripcio)
        val edtZoom = findViewById<EditText>(R.id.edtxtZoomMaps)
        val lstItinerariAdd = findViewById<ListView>(R.id.lstItinerariAdd)
        val btnAddItinerari= findViewById<Button>(R.id.btnAddItinerari)
        val btnAcceptarNouPaquet = findViewById<Button>(R.id.btnAcceptarNouPaquet)
        val btnAddPrincipalImg = findViewById<ImageButton>(R.id.btnAddPrincipalImg)
        val btnAddSecundariaImg = findViewById<ImageButton>(R.id.btnAddSecundariaImg)

        btnAddItinerari.setOnClickListener()
        {
            val myDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_input,null)

            val myBuilder = AlertDialog.Builder(this)
                .setView(myDialogView)
            val myAlertDialog = myBuilder.show()
            myAlertDialog.getWindow()?.setBackgroundDrawableResource(android.R.color.transparent)
            val btnGuardar =myDialogView.findViewById<Button>(R.id.btnGuardarNouItinerari)
            val cardDialog = myDialogView.findViewById<CardView>(R.id.cardDialog)
            val dialogNomIter =myDialogView.findViewById<EditText>(R.id.dialogNomIter)
            val dialogLatitudIter =myDialogView.findViewById<EditText>(R.id.dialogLatitudIter)
            val dialogLongitudIter =myDialogView.findViewById<EditText>(R.id.dialogLongitudIter)

            btnGuardar.setOnClickListener()
            {
                myAlertDialog.dismiss()
            }
        }

    }
}