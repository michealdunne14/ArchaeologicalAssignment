package com.example.archaeologicalfieldwork.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.archaeologicalfieldwork.R
import com.example.archaeologicalfieldwork.activities.ImageAdapter
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
            itemView.mCardName.text = hillfort.name
            itemView.mCardDescription.text = hillfort.description
            val viewPager = itemView.findViewById<ViewPager>(R.id.mCardImageList)
            val adapter = ImageAdapter(itemView.context,hillfort.imageStore)
            viewPager.adapter = adapter
//            itemView.mCardImage.setImageBitmap(readImageFromPath(itemView.context,hillfort.image))
            itemView.setOnClickListener { listener.onHillFortClick(hillfort) }
        }
    }
}