package com.example.politravel_mariomolina

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_llistat)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.title = "";

        val lstPaquets = findViewById<RecyclerView>(R.id.lstPaquets)
        var paquets = FilesManager.getPaquets(this)
        var adapter = PaquetsAdapter(this,paquets)
        lstPaquets.hasFixedSize()
        lstPaquets.layoutManager = LinearLayoutManager(this)
        lstPaquets.adapter=adapter



        val btnAdd = findViewById<FloatingActionButton>(R.id.addButton)

        btnAdd.setOnClickListener(){
            val intent = Intent(this,AddActivity::class.java)
            startActivity(intent)
        }

        adapter.setOnClickListener()
        {
            val intent = Intent(this,DetailActivity::class.java)
            val paquet = paquets[lstPaquets.getChildAdapterPosition(it)]
            intent.putExtra(DetailActivity.paquetConstants.PAQUET,paquet)
            startActivity(intent)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.llistat_menu, menu)
        return true
    }
}