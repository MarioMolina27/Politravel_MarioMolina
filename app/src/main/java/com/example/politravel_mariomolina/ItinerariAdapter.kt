package com.example.politravel_mariomolina

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class ItinerariAdapter(context: Context, val layout: Int, val itinerari: MutableList<Itinerari>):
    ArrayAdapter<Itinerari>(context, layout, itinerari)
{
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View

        if (convertView !=null){
            view = convertView
        }
        else{
            view =
                LayoutInflater.from(getContext()).inflate(layout, parent, false)
        }
        bindPlanet(view,itinerari[position])
        return view
    }

    fun bindPlanet(view: View, itinerari: Itinerari){

        val txtNomItinerari = view.findViewById<TextView>(R.id.txtNomItinerari)
        txtNomItinerari.text= itinerari.nom
    }
}
