package com.example.politravel_mariomolina.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.politravel_mariomolina.*
import com.example.politravel_mariomolina.adapters.PaquetsAdapter
import com.example.politravel_mariomolina.datamodel.Keys
import com.example.politravel_mariomolina.datamodel.Paquet
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

class MainActivity : AppCompatActivity() {

    companion object {
        var paquets = mutableListOf<Paquet>()
    }
    private lateinit var lstPaquets: RecyclerView
    private lateinit var adapter: PaquetsAdapter

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

        paquets = FilesManager.getPaquets(this)
        lstPaquets = findViewById(R.id.lstPaquets)
        adapter = PaquetsAdapter(this,paquets)

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
            val paquet = adapter.returnList()[lstPaquets.getChildAdapterPosition(it)]
            intent.putExtra(Keys.paquetConstants.PAQUET,paquet)
            startActivity(intent)
        }

        adapter.setOnLongClickListener()
        {
            val intent = Intent(this, AddActivity::class.java)
            val paquet = adapter.returnList()[lstPaquets.getChildAdapterPosition(it)]
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
        val  item = menu?.findItem(R.id.search_button)
        val searchView = item?.actionView as SearchView
        val titleToolBar = findViewById<TextView>(R.id.titleToolBar)

        searchView.setOnSearchClickListener {
            titleToolBar.visibility = View.GONE
        }

        searchView.setOnCloseListener {
            titleToolBar.visibility = View.VISIBLE
            false
        }
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }
        })

        return super.onCreateOptionsMenu(menu)
    }


    fun filterList(query: String?)
    {
        if(query != null)
        {
            val filteredList = mutableListOf<Paquet>()
            for(i in paquets)
            {
                if(i.nomPaquet.lowercase(Locale.ROOT).contains(query))
                {
                    filteredList.add(i)
                }
            }

            if (!filteredList.isEmpty())
            {
                adapter.setFilteredList(filteredList)
            }
        }
    }
}