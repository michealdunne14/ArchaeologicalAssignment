package com.example.archaeologicalfieldwork.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.archaeologicalfieldwork.R
import com.example.archaeologicalfieldwork.models.HillFortModel
import kotlinx.android.synthetic.main.card_list.view.*

interface HillFortListener {
    fun onHillFortClick(hillfort: HillFortModel)
}

class HillFortAdapter constructor(private var hillforts: List<HillFortModel>,
                                  private val listener:HillFortListener)
                                  : RecyclerView.Adapter<HillFortAdapter.MainHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        return MainHolder(
            LayoutInflater.from(parent.context).inflate(
            R.layout.card_list,
            parent,
            false
        )
        )
    }

    override fun getItemCount(): Int = hillforts.size

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val hillfort = hillforts[holder.adapterPosition]
        holder.bind(hillfort,listener)
    }


    class MainHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView){

        fun bind(hillfort: HillFortModel,listener: HillFortListener) {
            itemView.cardName.text = hillfort.name
            itemView.cardDescription.text = hillfort.description
            itemView.setOnClickListener { listener.onHillFortClick(hillfort) }
        }
    }
}