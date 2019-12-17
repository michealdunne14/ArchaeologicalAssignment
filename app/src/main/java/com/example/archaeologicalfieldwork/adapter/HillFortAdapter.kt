package com.example.archaeologicalfieldwork.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.archaeologicalfieldwork.R
import com.example.archaeologicalfieldwork.activities.BaseFragment.BaseFragmentPresenter
import com.example.archaeologicalfieldwork.animation.Bounce
import com.example.archaeologicalfieldwork.models.HillFortModel
import com.example.archaeologicalfieldwork.models.Images
import com.example.archaeologicalfieldwork.models.Notes
import com.example.archaeologicalfieldwork.models.UserModel
import com.example.archaeologicalfieldwork.models.jsonstore.generateRandomId
import kotlinx.android.synthetic.main.card_list.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

interface HillFortListener {
    fun onHillFortClick(hillfort: HillFortModel)
}

class HillFortAdapter(
    private var hillforts: List<HillFortModel>,
    private val listener: HillFortListener,
    private val baseFragmentPresenter: BaseFragmentPresenter,
    private val userModel: UserModel
) : RecyclerView.Adapter<HillFortAdapter.MainHolder>() {


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
        holder.bind(hillfort,listener,baseFragmentPresenter,userModel)
    }

    class MainHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView){
        val stringList: ArrayList<String> = ArrayList()
        fun bind(
            hillfort: HillFortModel,
            listener: HillFortListener,
            baseFragmentPresenter: BaseFragmentPresenter,
            userModel: UserModel
        ) {
//          Setting card Information
            doAsync {
                val images = baseFragmentPresenter.app.hillforts.findImages(hillfort.fbId)
                uiThread {
                    doFindImages(images,hillfort.fbId)
                }
            }
            itemView.mCardName.text = hillfort.name
            itemView.mCardDescription.text = hillfort.description
            itemView.mDate.text = hillfort.datevisted

            var visitedCheck = hillfort.visitCheck
            var starCheck = hillfort.starCheck

            val location = "Latitude " + hillfort.location.lat +
                    "\nLongitude" + hillfort.location.lng
            itemView.mCardLocation.text = location
//          Sends Note to card
            itemView.mCardSendButton.setOnClickListener {
                hillfort.notes.note = itemView.mCardNote.text.toString()
                hillfort.notes.hillfortNotesid = hillfort.id
                hillfort.notes.noteid = generateRandomId()
                doCreateNote(baseFragmentPresenter,hillfort.notes)
                itemView.mCardNote.text.clear()
            }

//          Visited Button
            if (visitedCheck){
                itemView.mCardCheckButton.setImageResource(R.mipmap.check_icon)
            }else{
                itemView.mCardCheckButton.setImageResource(R.mipmap.check_icon_clear)
            }

            if (starCheck){
                itemView.mCardStarButton.setImageResource(R.mipmap.star_fill)
            }else{
                itemView.mCardStarButton.setImageResource(R.mipmap.star_nofill)
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
                    doUpdateHillforts(hillfort,userModel,baseFragmentPresenter)
                }else{
                    val myAnim = AnimationUtils.loadAnimation(itemView.context, R.anim.bounce)
                    val interpolator = Bounce(0.2, 20.0)
                    myAnim.interpolator = interpolator
                    itemView.mCardCheckButton.startAnimation(myAnim)
                    itemView.mCardCheckButton.setImageResource(R.mipmap.check_icon_clear)
                    hillfort.visitCheck = false
                    doUpdateHillforts(hillfort,userModel,baseFragmentPresenter)
                }
            }

            itemView.mCardStarButton.setOnClickListener{
                starCheck = !starCheck
                if (starCheck) {
                    val myAnim = AnimationUtils.loadAnimation(itemView.context, R.anim.bounce)
                    val interpolator = Bounce(0.2, 20.0)
                    myAnim.interpolator = interpolator
                    itemView.mCardStarButton.startAnimation(myAnim)
                    itemView.mCardStarButton.setImageResource(R.mipmap.star_fill)
                    hillfort.starCheck = true
                    doUpdateHillforts(hillfort,userModel,baseFragmentPresenter)
                }else{
                    val myAnim = AnimationUtils.loadAnimation(itemView.context, R.anim.bounce)
                    val interpolator = Bounce(0.2, 20.0)
                    myAnim.interpolator = interpolator
                    itemView.mCardStarButton.startAnimation(myAnim)
                    itemView.mCardStarButton.setImageResource(R.mipmap.star_nofill)
                    hillfort.starCheck = false
                    doUpdateHillforts(hillfort,userModel,baseFragmentPresenter)
                }
            }
            itemView.setOnClickListener { listener.onHillFortClick(hillfort) }
        }

        fun doUpdateHillforts(hillfort: HillFortModel,userModel: UserModel,baseFragmentPresenter: BaseFragmentPresenter){
            doAsync {
                baseFragmentPresenter.app.hillforts.updateHillforts(hillfort, userModel)
            }
        }

        fun doFindImages(
            imagesList: List<Images>,
            fbId: String
        ) {
            val viewPager = itemView.findViewById<ViewPager>(R.id.mCardImageList)
            for (i in imagesList){
                if (i.hillfortFbid == fbId) {
                    stringList.add(i.image)
                }
            }
            if (stringList.isNotEmpty()) {
                val adapter = ImageAdapter(itemView.context, stringList)
                viewPager.adapter = adapter
            }
        }

        fun doCreateNote(
            baseFragmentPresenter: BaseFragmentPresenter,
            note: Notes
        ){
            doAsync {
                baseFragmentPresenter.app.hillforts.createNote(note)
            }
        }
    }
}