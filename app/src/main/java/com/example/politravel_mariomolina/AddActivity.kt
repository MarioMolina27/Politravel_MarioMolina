package com.example.politravel_mariomolina

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import java.security.Key
import kotlin.properties.Delegates

class AddActivity : AppCompatActivity() {

    private lateinit var imgLlista : String
    private lateinit var imgDetail : String
    private var position by Delegates.notNull<Int>()
    private val getResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult())
        {
            val btnAddPrincipalImg = findViewById<ShapeableImageView>(R.id.btnAddPrincipalImg)
            val btnAddSecundariaImg = findViewById<ShapeableImageView>(R.id.btnAddSecundariaImg)

            if(it.resultCode == RESULT_OK){
                val img = it.data?.getStringExtra(Keys.paquetConstants.IMG) as String
                val activity = it.data?.getStringExtra(Keys.paquetConstants.IMAGE_BUTTON) as String
                val imgPath = getFilesDir().toString()+ "/img/" + img
                val bitmap = BitmapFactory.decodeFile(imgPath)
                if(activity.equals("PRINCIPAL"))
                {
                    btnAddPrincipalImg?.setImageBitmap(bitmap)
                    imgDetail = img
                }
                else
                {
                    btnAddSecundariaImg?.setImageBitmap(bitmap)
                    imgLlista = img
                }
            }
            else if(it.resultCode== RESULT_CANCELED){
                Toast.makeText(this,"Operació d'afegir dades cancel·lada", Toast.LENGTH_SHORT).show()
            }
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mod_add_layout)
        setSupportActionBar(findViewById(R.id.toolbarAdd))
        supportActionBar?.title = "";

        val intent = getIntent()
        val isNew = intent.getBooleanExtra(Keys.paquetConstants.IS_NEW, true)
        val edtNomPaquet = findViewById<EditText>(R.id.edtxtNomPaquet)
        val edtDuracio = findViewById<EditText>(R.id.edtxtDuracio)
        val edtDescripcio = findViewById<EditText>(R.id.edtxtDescripcio)
        val edtZoom = findViewById<EditText>(R.id.edtxtZoomMaps)
        val lstItinerariAdd = findViewById<RecyclerView>(R.id.lstItinerariAdd)
        val btnAddItinerari= findViewById<Button>(R.id.btnAddItinerari)
        val btnAcceptarNouPaquet = findViewById<Button>(R.id.btnAcceptarNouPaquet)
        val btnAddPrincipalImg = findViewById<ShapeableImageView>(R.id.btnAddPrincipalImg)
        val btnAddSecundariaImg = findViewById<ShapeableImageView>(R.id.btnAddSecundariaImg)
        var paquet: Paquet


        val transport = mutableListOf<Transport>(
            Transport("---",R.drawable.flecha),
            Transport("Avió",R.drawable.plane),
            Transport("Bus",R.drawable.bus),
            Transport("Tren",R.drawable.train))

        var itinerari = mutableListOf<Itinerari>()

        val lstTransport = findViewById<Spinner>(R.id.lstTransport)
        val adapter = TransportAdapter(this,R.layout.transport_item,transport)
        lstTransport.adapter=adapter
        adapter.setDropDownViewResource(R.layout.transport_item)


        if(!isNew)
        {
            paquet = intent.getSerializableExtra(Keys.paquetConstants.PAQUET) as Paquet
            position = intent.getIntExtra(Keys.paquetConstants.SEND_POSITION,0)
            edtNomPaquet.setText(paquet.nomPaquet)
            edtDuracio.setText(paquet.dies.toString())
            edtDescripcio.setText(paquet.descripcio)
            edtZoom.setText(paquet.grausGoogleMaps.toString())
            imgDetail = paquet.imgDetail
            imgLlista = paquet.imgLlista
            val imgPathDetail = getFilesDir().toString()+ "/img/" + imgDetail
            val bitmapDetail = BitmapFactory.decodeFile(imgPathDetail)
            btnAddPrincipalImg?.setImageBitmap(bitmapDetail)
            val imgPathLlista = getFilesDir().toString()+ "/img/" + imgLlista
            val bitmapLlista = BitmapFactory.decodeFile(imgPathLlista)
            btnAddSecundariaImg?.setImageBitmap(bitmapLlista)
            itinerari = paquet.itinerari

            if(paquet.transport.equals("Avio"))
            {
                lstTransport.setSelection(1)
            }
            else if(paquet.transport.equals("Bus"))
            {
                lstTransport.setSelection(2)
            }
            else
            {
                lstTransport.setSelection(3)
            }
        }

        var adapterItinerari = ItinerariAdapter(this,itinerari)
        lstItinerariAdd.adapter=adapterItinerari
        lstItinerariAdd.hasFixedSize()
        lstItinerariAdd.layoutManager = LinearLayoutManager(this)
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
        }
        btnAcceptarNouPaquet.setOnClickListener()
        {
            val size = itinerari.size -1
            val selectedTransport = lstTransport.selectedItem as Transport
            val paquet = Paquet(1,edtNomPaquet.text.toString(),edtDescripcio.text.toString(),imgLlista,imgDetail
                ,selectedTransport.nom,itinerari[0].nom,itinerari[0].longitud,itinerari[0].latitud,edtZoom.text.toString().toDouble(),itinerari[size].nom,edtDuracio.text.toString().toInt(),itinerari)
            val intent = Intent(this,MainActivity::class.java)
            intent.putExtra(Keys.paquetConstants.RETORN, paquet)
            intent.putExtra(Keys.paquetConstants.IS_NEW_RETORN,isNew)
            if(!isNew)
            {
                intent.putExtra(Keys.paquetConstants.RETORN_POSITION,position)
            }
            setResult(RESULT_OK,intent)
            finish()
        }
        btnAddSecundariaImg.setOnClickListener()
        {
            val intent = Intent(this,GalleryActivity::class.java)
            intent.putExtra(Keys.paquetConstants.ADD_TO_IMG,"SECUNDARIA")
            getResult.launch(intent)
        }
        btnAddPrincipalImg.setOnClickListener()
        {
            val intent = Intent(this,GalleryActivity::class.java)
            intent.putExtra(Keys.paquetConstants.ADD_TO_IMG,"PRINCIPAL")
            getResult.launch(intent)
        }

    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.mod_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when(item.itemId)
        {
            R.id.delete ->
            {
                val intent = Intent(this,MainActivity::class.java)
                intent.putExtra(Keys.paquetConstants.DELETE_PACKAGE,true)
                intent.putExtra(Keys.paquetConstants.POSTION_DELETE_PACKAGE,position)
                setResult(RESULT_OK,intent)
                finish()
                return true
            }
            else -> {
                return true
            }
        }


        return super.onOptionsItemSelected(item)
    }
}