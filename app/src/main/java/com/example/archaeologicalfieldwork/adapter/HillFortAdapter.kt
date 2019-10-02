package com.example.archaeologicalfieldwork.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView

interface HillFortListener {
    fun onPlacemarkClick(placemark: HillFortListener)
}

class HillFortAdapter constructor(private var hillforts: List<HillFortAdapter>,private val listener:HillFortListener){



    class MainHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView){

        fun bind(placemark: HillFortListener,listener: HillFortListener) {
//            itemView.pl
        }
    }
}