package com.example.politravel_mariomolina.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.politravel_mariomolina.*
import com.example.politravel_mariomolina.adapters.PaquetsAdapter
import com.example.politravel_mariomolina.datamodel.Keys
import com.example.politravel_mariomolina.datamodel.Paquet
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    companion object {
        var paquets = mutableListOf<Paquet>()
    }

    private val getResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult())
        {
            val lstPaquets = findViewById<RecyclerView>(R.id.lstPaquets)
            if(it.resultCode == RESULT_OK){
                //En cas que el paquet no s'eliminarà farem les condicions per guardar o modificar un paquet
                if(it.data?.getBooleanExtra(Keys.paquetConstants.DELETE_PACKAGE,false) == false)
                {
                    //Rebrem sempre un paquet i una variable booleana que indica si el paquet existeix o no
                    val paquet = it.data?.getSerializableExtra(Keys.paquetConstants.RETORN) as Paquet
                    val isNew = it.data?.getBooleanExtra(Keys.paquetConstants.IS_NEW_RETORN,true)
                    if(isNew == true) //En cas que sigui un paquet nou s'afegeix a la llista
                    {
                        paquets.add(paquet)
                    }
                    else // En cas que no sobrescriu la posició amb el nou paquet
                    {
                        val position = it.data?.getIntExtra(Keys.paquetConstants.RETORN_POSITION,0)
                        paquets[position!!] = paquet
                    }
                }
                //En cas que eliminem un paquet rebrem la seva posició i l'eliminarem de la llista
               else
               {
                   val position = it.data?.getIntExtra(Keys.paquetConstants.POSTION_DELETE_PACKAGE,-1)
                   if (position != null) {
                       if (position > -1) {
                           Toast.makeText(this,"Paquet "+paquets[position].nomPaquet+ " eliminat!",Toast.LENGTH_LONG).show()
                           paquets.removeAt(position)
                       }
                       else
                       {
                           Toast.makeText(this,"No es pot eliminar un paquet que no està creat",Toast.LENGTH_SHORT).show()
                       }
                   }
               }
                //Actualitzem l'adapter després de tornar d'alguna activity i rescriurem el json amb la llista
                lstPaquets.adapter?.notifyDataSetChanged()
                FilesManager.savePaquets(this,paquets)
            }
            else if(it.resultCode== RESULT_CANCELED){
                Toast.makeText(this,"Operació d'afegir dades cancel·lada", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_llistat)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.title = "";

        val lstPaquets = findViewById<RecyclerView>(R.id.lstPaquets)
        paquets = FilesManager.getPaquets(this)
        var adapter = PaquetsAdapter(this,paquets)
        lstPaquets.hasFixedSize()
        lstPaquets.layoutManager = LinearLayoutManager(this)
        lstPaquets.adapter=adapter


        val btnAdd = findViewById<FloatingActionButton>(R.id.addButton)

        btnAdd.setOnClickListener(){
            val intent = Intent(this, AddActivity::class.java)

            getResult.launch(intent)
        }

        adapter.setOnClickListener()
        {
            val intent = Intent(this, DetailActivity::class.java)
            val paquet = paquets[lstPaquets.getChildAdapterPosition(it)]
            intent.putExtra(Keys.paquetConstants.PAQUET,paquet)
            startActivity(intent)
        }

        adapter.setOnLongClickListener()
        {
            val intent = Intent(this, AddActivity::class.java)
            val paquet = paquets[lstPaquets.getChildAdapterPosition(it)]
            val position = lstPaquets.getChildAdapterPosition(it)
            intent.putExtra(Keys.paquetConstants.PAQUET,paquet)
            intent.putExtra(Keys.paquetConstants.IS_NEW,false)
            intent.putExtra(Keys.paquetConstants.SEND_POSITION,position)
            getResult.launch(intent)
            true
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.llistat_menu, menu)
        return true
    }
}