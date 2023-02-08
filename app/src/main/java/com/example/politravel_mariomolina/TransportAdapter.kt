package com.example.politravel_mariomolina

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TransportAdapter(context: Context,val layout: Int, val transports: MutableList<Transport>):
    ArrayAdapter<Transport>(context, layout, transports)
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
        bindTransport(view,transports[position])
        return view
    }
    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View

        if (convertView !=null){
            view = convertView
        }
        else{
            view =
                LayoutInflater.from(getContext()).inflate(layout, parent, false)
        }
        bindTransport(view,transports[position])
        return view
    }

    fun bindTransport(view: View, transport: Transport){
        val imgLstTransport = view.findViewById<ImageView>(R.id.imgLstTransport)
        imgLstTransport.setImageResource(transport.img)

        val txtLstTransport = view.findViewById<TextView>(R.id.txtLstTransport)
        txtLstTransport.text= transport.nom

    }
}