package com.example.politravel_mariomolina

import android.app.Activity
import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.imageview.ShapeableImageView

class GalleryAdapter(context: Context, val layout: Int, val names: Array<String>):
    ArrayAdapter<String>(context, layout, names) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View

        if (convertView != null) {
            view = convertView
        } else {
            view =
                LayoutInflater.from(getContext()).inflate(layout, parent, false)
        }
        bindPlanet(view, names[position])
        return view
    }

    fun bindPlanet(view: View, name: String) {

        val imgGallery = view.findViewById<ShapeableImageView>(R.id.imgGallery)
        val imgPath = context.getFilesDir().toString()+ "/img/" + name
        val bitmap = BitmapFactory.decodeFile(imgPath)
        imgGallery?.setImageBitmap(bitmap)
    }
}