package com.example.archaeologicalfieldwork.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.archaeologicalfieldwork.R
import com.example.archaeologicalfieldwork.activities.BaseFragment.BaseFragmentPresenter
import com.example.archaeologicalfieldwork.activities.Database.HillfortFireStore
import com.example.archaeologicalfieldwork.animation.Bounce
import com.example.archaeologicalfieldwork.models.HillFortModel
import com.example.archaeologicalfieldwork.models.Images
import com.example.archaeologicalfieldwork.models.Notes
import com.example.archaeologicalfieldwork.models.UserModel
import com.example.archaeologicalfieldwork.models.jsonstore.generateRandomId
import kotlinx.android.synthetic.main.card_list.view.*
import org.jetbrains.anko.doAsync

interface HillFortListener {
    fun onHillFortClick(
        hillfort: HillFortModel,
        images: ArrayList<Images>
    )
}

class HillFortAdapter(
    private var hillforts: List<HillFortModel>,
    private val listener: HillFortListener,
    private val baseFragmentPresenter: BaseFragmentPresenter,
    private val userModel: UserModel,
    private val images: ArrayList<Images>
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
        holder.bind(hillfort,listener,baseFragmentPresenter,userModel,images)
    }

    class MainHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView){

        var fireStore: HillfortFireStore? = null


        val stringList: ArrayList<String> = ArrayList()
        fun bind(
            hillfort: HillFortModel,
            listener: HillFortListener,
            baseFragmentPresenter: BaseFragmentPresenter,
            userModel: UserModel,
            images: ArrayList<Images>
        ) {
            if (baseFragmentPresenter.app.hillforts is HillfortFireStore) {
                fireStore = baseFragmentPresenter.app.hillforts as HillfortFireStore
            }
//          Setting card Information
            doFindImages(images,hillfort.fbId)
            itemView.mCardName.text = hillfort.name
            itemView.mCardDescription.text = hillfort.description
            itemView.mDate.text = hillfort.datevisted
            itemView.mRating.text = hillfort.rating

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
                    doUpdateHillforts(hillfort, fireStore)
                }else{
                    val myAnim = AnimationUtils.loadAnimation(itemView.context, R.anim.bounce)
                    val interpolator = Bounce(0.2, 20.0)
                    myAnim.interpolator = interpolator
                    itemView.mCardCheckButton.startAnimation(myAnim)
                    itemView.mCardCheckButton.setImageResource(R.mipmap.check_icon_clear)
                    hillfort.visitCheck = false
                    doUpdateHillforts(hillfort, fireStore)
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
                    doUpdateHillforts(hillfort, fireStore)
                }else{
                    val myAnim = AnimationUtils.loadAnimation(itemView.context, R.anim.bounce)
                    val interpolator = Bounce(0.2, 20.0)
                    myAnim.interpolator = interpolator
                    itemView.mCardStarButton.startAnimation(myAnim)
                    itemView.mCardStarButton.setImageResource(R.mipmap.star_nofill)
                    hillfort.starCheck = false
                    doUpdateHillforts(hillfort, fireStore)
                }
            }
            itemView.setOnClickListener { listener.onHillFortClick(hillfort,images) }
        }

        fun doUpdateHillforts(
            hillfort: HillFortModel,
            fireStore: HillfortFireStore?
        ){
            doAsync {
                fireStore?.starHillfort(hillfort)
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