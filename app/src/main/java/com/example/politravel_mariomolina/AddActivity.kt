package com.example.politravel_mariomolina

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.ShapeAppearanceModel

class AddActivity : AppCompatActivity() {
    private val getResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult())
        {
            val btnAddPrincipalImg = findViewById<ShapeableImageView>(R.id.btnAddPrincipalImg)
            val btnAddSecundariaImg = findViewById<ShapeableImageView>(R.id.btnAddSecundariaImg)

            if(it.resultCode == RESULT_OK){
                val img = it.data?.getStringExtra(MainActivity.paquetConstants.IMG) as String
                val activity = it.data?.getStringExtra(MainActivity.paquetConstants.IMAGE_BUTTON) as String
                val imgPath = getFilesDir().toString()+ "/img/" + img
                val bitmap = BitmapFactory.decodeFile(imgPath)
                if(activity.equals("PRINCIPAL"))
                {
                    btnAddPrincipalImg?.setImageBitmap(bitmap)
                }
                else
                {
                    btnAddSecundariaImg?.setImageBitmap(bitmap)
                }


            }
            else if(it.resultCode== RESULT_CANCELED){
                Toast.makeText(this,"Operació d'afegir dades cancel·lada", Toast.LENGTH_SHORT).show()
            }
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mod_add_layout)

        val transport = mutableListOf<Transport>(
            Transport("---",R.drawable.flecha),
            Transport("Avió",R.drawable.plane),
            Transport("Bus",R.drawable.bus),
            Transport("Tren",R.drawable.train))

        val itinerari = mutableListOf<Itinerari>()

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


        var adapterItinerari = ItinerariAdapter(this,R.layout.iter_item,itinerari)
        lstItinerariAdd.adapter=adapterItinerari


        // Codi del Dialog per afegir un nou lloc a l'itinerari
        btnAddItinerari.setOnClickListener()
        {
            val myDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_input,null)

            val myBuilder = AlertDialog.Builder(this)
                .setView(myDialogView)
            val myAlertDialog = myBuilder.show()
            myAlertDialog.getWindow()?.setBackgroundDrawableResource(android.R.color.transparent)
            val btnGuardar =myDialogView.findViewById<Button>(R.id.btnGuardarNouItinerari)
            val dialogNomIter =myDialogView.findViewById<EditText>(R.id.dialogNomIter)
            val dialogLatitudIter =myDialogView.findViewById<EditText>(R.id.dialogLatitudIter)
            val dialogLongitudIter =myDialogView.findViewById<EditText>(R.id.dialogLongitudIter)

            btnGuardar.setOnClickListener()
            {
                if(dialogNomIter.text.isNotEmpty() && dialogLatitudIter.text.isNotEmpty() && dialogLongitudIter.text.isNotEmpty())
                {
                    itinerari.add(Itinerari(dialogNomIter.text.toString(),dialogLongitudIter.text.toString().toDouble(),dialogLatitudIter.text.toString().toDouble()))
                    myAlertDialog.dismiss()
                    adapterItinerari.notifyDataSetChanged()
                }
                else
                {
                    Toast.makeText(this,"Tots els camps han d'estar omplerts per guardar un nou itinerari",Toast.LENGTH_SHORT).show()
                }
            }

            btnAcceptarNouPaquet.setOnClickListener()
            {
                val size = itinerari.size -1
                val selectedTransport = lstTransport.selectedItem as Transport
                val paquet = Paquet(1,edtNomPaquet.text.toString(),edtDescripcio.text.toString(),"prueba.jpg","prueba2.jpg",selectedTransport.nom,itinerari[0].nom,itinerari[0].longitud,itinerari[0].latitud,edtZoom.text.toString().toDouble(),itinerari[size].nom,edtDuracio.text.toString().toInt(),itinerari)
                val intent = Intent(this,MainActivity::class.java)
                intent.putExtra(MainActivity.paquetConstants.RETORN, paquet)
                setResult(RESULT_OK,intent)
                finish()
            }
        }

        btnAddSecundariaImg.setOnClickListener()
        {
            val intent = Intent(this,GalleryActivity::class.java)
            intent.putExtra(MainActivity.paquetConstants.ADD_TO_IMG,"SECUNDARIA")
            getResult.launch(intent)
        }
        btnAddPrincipalImg.setOnClickListener()
        {
            val intent = Intent(this,GalleryActivity::class.java)
            intent.putExtra(MainActivity.paquetConstants.ADD_TO_IMG,"PRINCIPAL")
            getResult.launch(intent)
        }

    }
}