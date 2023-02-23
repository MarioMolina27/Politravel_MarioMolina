package com.example.politravel_mariomolina.adapters

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.politravel_mariomolina.datamodel.Paquet
import com.example.politravel_mariomolina.R

class PaquetsAdapter(private val context: Context,
                     private var paquets: MutableList<Paquet>):
    RecyclerView.Adapter<PaquetsAdapter.PaquetsViewHolder>(), View.OnClickListener, View.OnLongClickListener
{
    private val layout = R.layout.item_layout
    private var clickListener: View.OnClickListener? = null
    private var longClickListener: View.OnLongClickListener? = null

    class PaquetsViewHolder(val view: View):
        RecyclerView.ViewHolder(view)
    {

        var txtNomPaquet: TextView
        var txtTransport: TextView
        var txtDuracio: TextView
        var imgPaquetDetail: ImageView

        init
        {
            txtNomPaquet = view.findViewById(R.id.txtNomPaquet)
            txtTransport= view.findViewById(R.id.txtTransport)
            txtDuracio=view.findViewById(R.id.txtDuracio)
            imgPaquetDetail=view.findViewById(R.id.imgPaquetDetail)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaquetsViewHolder
    {
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        view.setOnClickListener(this)
        view.setOnLongClickListener(this)
        return PaquetsViewHolder(view)
    }

    override fun onBindViewHolder(holder: PaquetsViewHolder, position: Int)
    {
        val paquet = paquets[position]
        bindPaquet(holder,paquet)
    }
    fun bindPaquet(holder: PaquetsViewHolder, paquet: Paquet)
    {
        val paquetPath = context.getFilesDir().toString()+ "/img/" + paquet.imgLlista
        val bitmap = BitmapFactory.decodeFile(paquetPath)
        holder.imgPaquetDetail?.setImageBitmap(bitmap)
        holder.txtNomPaquet?.text = paquet.nomPaquet
        holder.txtTransport?.text = paquet.transport
        holder.txtDuracio?.text = paquet.dies.toString()+" dies"
    }
    override fun getItemCount() = paquets.size

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

    fun setFilteredList(list: MutableList<Paquet>)
    {
        this.paquets = list
        notifyDataSetChanged()
    }

    fun returnList(): MutableList<Paquet>
    {
        return paquets
    }

}