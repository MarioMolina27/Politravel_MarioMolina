package com.example.politravel_mariomolina.activities

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
import com.example.politravel_mariomolina.*
import com.example.politravel_mariomolina.adapters.ItinerariAdapter
import com.example.politravel_mariomolina.adapters.TransportAdapter
import com.example.politravel_mariomolina.datamodel.Itinerari
import com.example.politravel_mariomolina.datamodel.Keys
import com.example.politravel_mariomolina.datamodel.Paquet
import com.example.politravel_mariomolina.datamodel.Transport
import com.google.android.material.imageview.ShapeableImageView
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


        /**
         * Fem referència a tots els elements del layout amb els findViewById
         * */
        val edtNomPaquet = findViewById<EditText>(R.id.edtxtNomPaquet)
        val edtDuracio = findViewById<EditText>(R.id.edtxtDuracio)
        val edtDescripcio = findViewById<EditText>(R.id.edtxtDescripcio)
        val edtZoom = findViewById<EditText>(R.id.edtxtZoomMaps)
        val lstItinerariAdd = findViewById<RecyclerView>(R.id.lstItinerariAdd)
        val btnAddItinerari= findViewById<Button>(R.id.btnAddItinerari)
        val btnAcceptarNouPaquet = findViewById<Button>(R.id.btnAcceptarNouPaquet)
        val btnAddPrincipalImg = findViewById<ShapeableImageView>(R.id.btnAddPrincipalImg)
        val btnAddSecundariaImg = findViewById<ShapeableImageView>(R.id.btnAddSecundariaImg)
        val lstTransport = findViewById<Spinner>(R.id.lstTransport)

        /**
         * Rebem l'intent i amb ell una variable booleana que ens indica si el paquet es nou o estem editant un paquet
         * */
        val intent = getIntent()
        position = -1
        val isNew = intent.getBooleanExtra(Keys.paquetConstants.IS_NEW, true)

        var paquet = Paquet()
        var itinerari = mutableListOf<Itinerari>()


        val transport = mutableListOf<Transport>(
            Transport("---", R.drawable.flecha),
            Transport("Avió", R.drawable.plane),
            Transport("Bus", R.drawable.bus),
            Transport("Tren", R.drawable.train)
        )


        val adapter = TransportAdapter(this, R.layout.transport_item,transport)
        lstTransport.adapter=adapter
        adapter.setDropDownViewResource(R.layout.transport_item)

        /**
         * En cas que estiguem editant un paquet existent, el rebrem i complirem tots els camps de l'activity
         * */
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


        btnAddItinerari.setOnClickListener()
        {
            showDialogNewItinerari(itinerari,adapterItinerari);
        }

        adapterItinerari.setOnLongClickListener()
        {
            val posicio = lstItinerariAdd.getChildAdapterPosition(it)
            Toast.makeText(this,"Itinerari "+itinerari[posicio].nom+" eliminat!",Toast.LENGTH_SHORT).show()
            itinerari.removeAt(posicio)
            adapterItinerari.notifyDataSetChanged()
            true
        }

        /**
         * Event de clicar al botó d'acceptar el paquet, retornarà un variable que indica si es tracta d'un nou paquet, en cas que sigui un nou enviarà només el paquet,
         * en cas contrari enviarà el paquet i la posició del paquet a modificar
         * */
        btnAcceptarNouPaquet.setOnClickListener()
        {
            val selectedTransport = lstTransport.selectedItem as Transport
            if(!edtNomPaquet.text.isNullOrEmpty() && !edtDescripcio.text.isNullOrEmpty() && !edtZoom.text.isNullOrEmpty()
                && !imgDetail.isNullOrEmpty()&&!imgLlista.isNullOrEmpty()&&!edtDuracio.text.isNullOrEmpty()&& selectedTransport.nom != "---"
            )
            {
                if(itinerari.size>0)
                {
                    var id= 0
                    val size = itinerari.size -1
                    if(isNew){id= newPaquetID()}
                    else{id = paquet.idPaquet}
                    val paquet = Paquet(id,edtNomPaquet.text.toString(),edtDescripcio.text.toString(),imgLlista,imgDetail
                        ,selectedTransport.nom,itinerari[0].nom,itinerari[0].longitud,itinerari[0].latitud,edtZoom.text.toString().toDouble(),itinerari[size].nom,edtDuracio.text.toString().toInt(),itinerari)
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra(Keys.paquetConstants.RETORN, paquet)
                    intent.putExtra(Keys.paquetConstants.IS_NEW_RETORN,isNew)
                    if(!isNew)
                    {
                        intent.putExtra(Keys.paquetConstants.RETORN_POSITION,position)
                    }
                    setResult(RESULT_OK,intent)
                    finish()
                }
                else
                {
                    Toast.makeText(this,"S'han d'afegir més de 2 itineraris ja que el paquet ha de tenir un inci i un final",Toast.LENGTH_LONG).show()
                }
            }
            else
            {
                Toast.makeText(this,"S'han d'omplir tots els camps per afegir un nou paquet",Toast.LENGTH_LONG).show()
            }

        }

        /**
         * Events per cridar a l'activity de Gallery per seleccionar imatges pel paquet
         * */
        btnAddSecundariaImg.setOnClickListener()
        {
            val intent = Intent(this, GalleryActivity::class.java)
            intent.putExtra(Keys.paquetConstants.ADD_TO_IMG,"SECUNDARIA")
            getResult.launch(intent)
        }
        btnAddPrincipalImg.setOnClickListener()
        {
            val intent = Intent(this, GalleryActivity::class.java)
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
                showDialogDelete();
                return true
            }
            else -> {
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * Funció que crida a un alert dialog personalitzat per afegir un nou itinerari
     * */
    fun showDialogNewItinerari(itinerari: MutableList<Itinerari>, adapterItinerari: ItinerariAdapter)
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
    /**
     * Funció que mostra un dialog per indicar si vols eliminar un paquet definitivament
     * */
    fun showDialogDelete()
    {
        val builder = AlertDialog.Builder(this, R.style.AlertDialogTheme)

        builder.setTitle("¿Eliminar este elemento?")
        builder.setMessage("¿Estás seguro de que deseas eliminar este elemento?")

        builder.setPositiveButton("Aceptar") { dialog, which ->
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra(Keys.paquetConstants.DELETE_PACKAGE,true)
            intent.putExtra(Keys.paquetConstants.POSTION_DELETE_PACKAGE,position)
            setResult(RESULT_OK,intent)
            finish()
        }

        builder.setNegativeButton("Cancel·lar") { dialog, which ->
            Toast.makeText(this,"Paquet no eliminat",Toast.LENGTH_SHORT).show()
        }
        val dialog = builder.create()
        dialog.show()
    }

    fun newPaquetID(): Int {
        return MainActivity.paquets[MainActivity.paquets.size-1].idPaquet +1
    }

}