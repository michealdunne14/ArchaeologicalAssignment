package com.example.archaeologicalfieldwork.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.example.archaeologicalfieldwork.R
import com.example.archaeologicalfieldwork.animation.Bounce
import com.example.archaeologicalfieldwork.fragment.HomeFragPresenter
import com.example.archaeologicalfieldwork.models.HillFortModel
import com.example.archaeologicalfieldwork.models.Notes
import com.example.archaeologicalfieldwork.models.UserModel
import com.example.archaeologicalfieldwork.models.jsonstore.generateRandomId
import kotlinx.android.synthetic.main.card_list.view.*
import org.jetbrains.anko.doAsync
import java.util.*

interface HillFortListener {
    fun onHillFortClick(hillfort: HillFortModel)
}

class HillFortAdapter(
    private var hillforts: List<HillFortModel>,
    private val listener: HillFortListener,
    private val homeFragPresenter: HomeFragPresenter,
    private val userModel: UserModel
)
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
        holder.bind(hillfort,listener,homeFragPresenter,userModel)
    }

    class MainHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView){

        fun bind(hillfort: HillFortModel,listener: HillFortListener,homeFragPresenter: HomeFragPresenter,userModel: UserModel) {
//          Setting card Information
            itemView.mCardName.text = hillfort.name
            itemView.mCardDescription.text = hillfort.description
            itemView.mDate.text = hillfort.datevisted

            var visitedCheck = hillfort.visitCheck

            val location = "Latitude " + hillfort.location.lat +
                    "\nLongitude" + hillfort.location.lng
            itemView.mCardLocation.text = location
//          Sends Note to card
            itemView.mCardSendButton.setOnClickListener {
                hillfort.notes.note = itemView.mCardNote.text.toString()
                hillfort.notes.hillfortNotesid = hillfort.id
                hillfort.notes.noteid = generateRandomId()
                doCreateNote(homeFragPresenter,hillfort.notes)
                itemView.mCardNote.text.clear()
            }
//          Visited Button
            if (visitedCheck){
                itemView.mCardCheckButton.setImageResource(R.mipmap.check_icon)
            }else{
                itemView.mCardCheckButton.setImageResource(R.mipmap.check_icon_clear)
            }
//          Check Box
            itemView.mCardCheckButton.setOnClickListener {
                visitedCheck = !visitedCheck
                if (visitedCheck) {
                    val myAnim = AnimationUtils.loadAnimation(itemView.context, R.anim.bounce)
                    val interpolator = Bounce(0.2, 20.0)
                    myAnim.interpolator = interpolator
                    itemView.mCardCheckButton.startAnimation(myAnim)
                    itemView.mCardCheckButton.setImageResource(R.mipmap.check_icon)
                    hillfort.visitCheck = true
                    doUpdateHillforts(hillfort,userModel,homeFragPresenter)
                }else{
                    val myAnim = AnimationUtils.loadAnimation(itemView.context, R.anim.bounce)
                    val interpolator = Bounce(0.2, 20.0)
                    myAnim.interpolator = interpolator
                    itemView.mCardCheckButton.startAnimation(myAnim)
                    itemView.mCardCheckButton.setImageResource(R.mipmap.check_icon_clear)
                    hillfort.visitCheck = false
                    doUpdateHillforts(hillfort,userModel,homeFragPresenter)
                }
            }

//            val viewPager = itemView.findViewById<ViewPager>(R.id.mCardImageList)
//            val adapter = ImageAdapter(
//                itemView.context,
//                hillfort.image
//            )
//            viewPager.adapter = adapter
            itemView.setOnClickListener { listener.onHillFortClick(hillfort) }
        }

        fun doUpdateHillforts(hillfort: HillFortModel,userModel: UserModel,homeFragPresenter: HomeFragPresenter){
            doAsync {
                homeFragPresenter.app.hillforts.updateHillforts(hillfort, userModel)
            }
        }
        fun doCreateNote(
            homeFragPresenter: HomeFragPresenter,
            note: Notes
        ){
            doAsync {
                homeFragPresenter.createNotes(note)
            }
        }
    }
}