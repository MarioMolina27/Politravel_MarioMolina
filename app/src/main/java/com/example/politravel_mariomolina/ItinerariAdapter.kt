package com.example.politravel_mariomolina

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ItinerariAdapter(private val context: Context,
                     private val itinerari: MutableList<Itinerari>):
    RecyclerView.Adapter<ItinerariAdapter.ItinerariViewHolder>(), View.OnClickListener, View.OnLongClickListener
{
    private val layout = R.layout.iter_item
    private var clickListener: View.OnClickListener? = null
    private var longClickListener: View.OnLongClickListener? = null

    class ItinerariViewHolder(val view: View):
        RecyclerView.ViewHolder(view)
    {

        var txtNomItinerari: TextView


        init
        {
            txtNomItinerari = view.findViewById(R.id.txtNomItinerari)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItinerariViewHolder
    {
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        view.setOnClickListener(this)
        view.setOnLongClickListener(this)
        return ItinerariViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItinerariViewHolder, position: Int)
    {
        val itinerari = itinerari[position]
        bindItinerari(holder,itinerari)
    }
    fun bindItinerari(holder: ItinerariViewHolder, itinerari: Itinerari)
    {
        holder?.txtNomItinerari.text = itinerari.nom
    }
    override fun getItemCount() = itinerari.size

    fun setOnClickListener(listener: View.OnClickListener)
    {
        clickListener = listener
    }
    fun setOnLongClickListener(listener: View.OnLongClickListener)
    {
        longClickListener = listener
    }

    override fun onClick(view: View?)
    {
        clickListener?.onClick(view)
    }

    override fun onLongClick(view: View?): Boolean {
        longClickListener?.onLongClick(view)
        return true
    }

}
