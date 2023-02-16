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

    object paquetConstants
    {
        const val  PAQUET = "PAQUET"
        const val  RETORN = "RETORN"
        const val  IMG = "IMG"
        const val ADD_TO_IMG = "ADD_TO_IMG"
        const val  IMAGE_BUTTON = "IMG_MAIN"
        const val IS_NEW = "IS_NEW"
    }
    private var paquets = mutableListOf<Paquet>()
    private val getResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult())
        {
            val lstPaquets = findViewById<RecyclerView>(R.id.lstPaquets)
            if(it.resultCode == RESULT_OK){
                val paquet = it.data?.getSerializableExtra(paquetConstants.RETORN) as Paquet
                paquets.add(paquet)
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
            intent.putExtra(paquetConstants.PAQUET,paquet)
            startActivity(intent)
        }

        adapter.setOnLongClickListener()
        {
            val intent = Intent(this,AddActivity::class.java)
            val paquet = paquets[lstPaquets.getChildAdapterPosition(it)]
            intent.putExtra(paquetConstants.PAQUET,paquet)
            intent.putExtra(paquetConstants.IS_NEW,false)
            startActivity(intent)
            true
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.llistat_menu, menu)
        return true
    }
}