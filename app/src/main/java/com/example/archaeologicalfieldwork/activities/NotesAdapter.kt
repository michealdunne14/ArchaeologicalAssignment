package com.example.archaeologicalfieldwork.activities

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.archaeologicalfieldwork.R
import com.example.archaeologicalfieldwork.models.HillFortModel
import kotlinx.android.synthetic.main.note_card.view.*

class NotesAdapter(private var notes: ArrayList<String>) : RecyclerView.Adapter<MainHolderNotes>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolderNotes {
        return MainHolderNotes(
            LayoutInflater.from(parent.context).inflate(
                R.layout.note_card,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int =  notes.size

    override fun onBindViewHolder(holder: MainHolderNotes, position: Int) {
        val notes = notes[holder.adapterPosition]
        holder.bind(notes)
    }

}

class MainHolderNotes(itemVew: View) : RecyclerView.ViewHolder(itemVew) {

    fun bind(notes: String) {
        itemView.mAddFortNote.text = notes
    }
}
