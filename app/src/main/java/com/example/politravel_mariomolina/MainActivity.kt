package com.example.politravel_mariomolina

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {


    private var paquets = mutableListOf<Paquet>()
    private val getResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult())
        {
            val lstPaquets = findViewById<RecyclerView>(R.id.lstPaquets)
            if(it.resultCode == RESULT_OK){
                if(it.data?.getBooleanExtra(Keys.paquetConstants.DELETE_PACKAGE,false) == false)
                {
                    val paquet = it.data?.getSerializableExtra(Keys.paquetConstants.RETORN) as Paquet
                    val isNew = it.data?.getBooleanExtra(Keys.paquetConstants.IS_NEW_RETORN,true)
                    if(isNew == true)
                    {
                        paquets.add(paquet)
                    }
                    else
                    {
                        val position = it.data?.getIntExtra(Keys.paquetConstants.RETORN_POSITION,0)
                        paquets[position!!] = paquet
                    }
                }
               else
               {
                   val position = it.data?.getIntExtra(Keys.paquetConstants.POSTION_DELETE_PACKAGE,-1)
                   if (position != null) {
                       if (position > -1) {
                           paquets.removeAt(position)
                       }
                       else
                       {
                           Toast.makeText(this,"No es pot eliminar un paquet que no està creat",Toast.LENGTH_SHORT).show()
                       }
                   }
               }
                lstPaquets.adapter?.notifyDataSetChanged()
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
            val intent = Intent(this,AddActivity::class.java)
            getResult.launch(intent)
        }

        adapter.setOnClickListener()
        {
            val intent = Intent(this,DetailActivity::class.java)
            val paquet = paquets[lstPaquets.getChildAdapterPosition(it)]
            intent.putExtra(Keys.paquetConstants.PAQUET,paquet)
            startActivity(intent)
        }

        adapter.setOnLongClickListener()
        {
            val intent = Intent(this,AddActivity::class.java)
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