package com.example.archaeologicalfieldwork.adapter

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.archaeologicalfieldwork.R
import com.example.archaeologicalfieldwork.activities.BaseFragment.BaseFragmentPresenter
import com.example.archaeologicalfieldwork.activities.Database.HillfortFireStore
import com.example.archaeologicalfieldwork.animation.Bounce
import com.example.archaeologicalfieldwork.models.HillFortModel
import com.example.archaeologicalfieldwork.models.Images
import com.example.archaeologicalfieldwork.models.UserModel
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
    private val images: ArrayList<Images>,
    private val user: UserModel
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

//  Item Count
    override fun getItemCount(): Int = hillforts.size

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val hillfort = hillforts[holder.adapterPosition]
        holder.bind(hillfort,listener,baseFragmentPresenter,images,user)
    }

    class MainHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView){
//      Set up fireStore
        var fireStore: HillfortFireStore? = null

        val stringList: ArrayList<String> = ArrayList()
        @SuppressLint("SetTextI18n")
        fun bind(
            hillfort: HillFortModel,
            listener: HillFortListener,
            baseFragmentPresenter: BaseFragmentPresenter,
            images: ArrayList<Images>,
            user: UserModel
        ) {
            if (baseFragmentPresenter.app.hillforts is HillfortFireStore) {
                fireStore = baseFragmentPresenter.app.hillforts as HillfortFireStore
            }
//          Setting card Information
            doFindImages(images,hillfort.fbId)
            itemView.mCardName.text = hillfort.name
            itemView.mCardDescription.text = hillfort.description
            itemView.mDate.text = hillfort.datevisted
            itemView.mRating.text = " " + hillfort.rating + " Stars"

            var visitedCheck = hillfort.visitCheck
            var starCheck = hillfort.starCheck

            val location = "Latitude " + hillfort.location.lat +
                    "\nLongitude" + hillfort.location.lng
            itemView.mCardLocation.text = location
//          Sends Note to card
            itemView.mCardSendButton.setOnClickListener {
                val note = itemView.mCardNote.text.toString()
                baseFragmentPresenter.doCreateNote(fireStore,note,hillfort)
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
                    baseFragmentPresenter.doLikeUpdateHillforts(hillfort)
                }else{
                    val myAnim = AnimationUtils.loadAnimation(itemView.context, R.anim.bounce)
                    val interpolator = Bounce(0.2, 20.0)
                    myAnim.interpolator = interpolator
                    itemView.mCardCheckButton.startAnimation(myAnim)
                    itemView.mCardCheckButton.setImageResource(R.mipmap.check_icon_clear)
                    hillfort.visitCheck = false
                    baseFragmentPresenter.doLikeUpdateHillforts(hillfort)
                }
            }
//          Share button
            itemView.mShareHillfort.setOnClickListener {
                val emailIntent = Intent(Intent.ACTION_SEND)
                emailIntent.data = Uri.parse("mailto:")
                emailIntent.type = "text/plain"
                emailIntent.putExtra(Intent.EXTRA_EMAIL, user.email)
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Hillfort " + hillfort.name)
                val arrayList = ArrayList<String>()
                for (i in images){
                    if (hillfort.fbId == i.hillfortFbid) {
                        arrayList.add(i.image + " \n")
                    }
                }
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Hillfort Name " + hillfort.name +" \n" +
                        "Hillfort Description " + hillfort.description +" \n" +
                        "Hillfort " + hillfort.location +" \n" +
                        "Hillfort date Visited " + hillfort.datevisted +" \n" +
                        "Hillfort Rating " + hillfort.rating +" \n" +
                        "Hillfort Star " + hillfort.starCheck +" \n" +
                        "Hillfort Visit Check " + hillfort.visitCheck +" \n" +
                        "Hillfort Visit Check " + arrayList +" \n")

                try {
                    itemView.context.startActivity(Intent.createChooser(emailIntent, "Send mail..."))
                    Log.i("Finished sending email.", "")
                } catch (e: ActivityNotFoundException) {
                    e.printStackTrace()
                }
            }
//          Star fill
            itemView.mCardStarButton.setOnClickListener{
                starCheck = !starCheck
                if (starCheck) {
                    val myAnim = AnimationUtils.loadAnimation(itemView.context, R.anim.bounce)
                    val interpolator = Bounce(0.2, 20.0)
                    myAnim.interpolator = interpolator
                    itemView.mCardStarButton.startAnimation(myAnim)
                    itemView.mCardStarButton.setImageResource(R.mipmap.star_fill)
                    hillfort.starCheck = true
                    baseFragmentPresenter.doStarUpdateHillforts(hillfort)
                }else{
                    val myAnim = AnimationUtils.loadAnimation(itemView.context, R.anim.bounce)
                    val interpolator = Bounce(0.2, 20.0)
                    myAnim.interpolator = interpolator
                    itemView.mCardStarButton.startAnimation(myAnim)
                    itemView.mCardStarButton.setImageResource(R.mipmap.star_nofill)
                    hillfort.starCheck = false
                    baseFragmentPresenter.doStarUpdateHillforts(hillfort)
                }
            }
            itemView.setOnClickListener { listener.onHillFortClick(hillfort,images) }
        }
//      Finds images and adds strings to image adapter
        fun doFindImages(imagesList: List<Images>, fbId: String) {
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
    }
}