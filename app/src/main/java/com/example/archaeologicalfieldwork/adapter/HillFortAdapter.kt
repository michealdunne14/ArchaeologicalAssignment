package com.example.archaeologicalfieldwork.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.archaeologicalfieldwork.R
import com.example.archaeologicalfieldwork.activities.ImageAdapter
import com.example.archaeologicalfieldwork.animation.Bounce
import com.example.archaeologicalfieldwork.main.MainApp
import com.example.archaeologicalfieldwork.models.HillFortModel
import com.google.android.gms.maps.SupportMapFragment
import kotlinx.android.synthetic.main.card_list.view.*

interface HillFortListener {
    fun onHillFortClick(hillfort: HillFortModel)
}

class HillFortAdapter(
    private var hillforts: List<HillFortModel>,
    private val listener: HillFortListener,
    private val app: MainApp
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
        holder.bind(hillfort,listener,app)
    }


    class MainHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView){

        fun bind(hillfort: HillFortModel,listener: HillFortListener,app: MainApp) {
            itemView.mCardName.text = hillfort.name
            itemView.mCardDescription.text = hillfort.description

            var visitedCheck = hillfort.visitCheck

            val location = "Latitude " + hillfort.location.lat +
                    "\nLongitude" + hillfort.location.lng
            itemView.mCardLocation.text = location
            itemView.mCardSendButton.setOnClickListener {
                hillfort.note.add(itemView.mCardNote.text.toString())
                app.hillforts.update(hillfort)
                itemView.mCardNote.text.clear()
            }

            if (visitedCheck){
                itemView.mCardCheckButton.setImageResource(R.mipmap.check_icon)
            }else{
                itemView.mCardCheckButton.setImageResource(R.mipmap.check_icon_clear)
            }

            itemView.mCardCheckButton.setOnClickListener {
                visitedCheck = !visitedCheck
                if (visitedCheck) {
                    val myAnim = AnimationUtils.loadAnimation(itemView.context, R.anim.bounce)
                    val interpolator = Bounce(0.2, 20.0)
                    myAnim.interpolator = interpolator
                    itemView.mCardCheckButton.startAnimation(myAnim)
                    itemView.mCardCheckButton.setImageResource(R.mipmap.check_icon)

                }else{
                    val myAnim = AnimationUtils.loadAnimation(itemView.context, R.anim.bounce)
                    val interpolator = Bounce(0.2, 20.0)
                    myAnim.interpolator = interpolator
                    itemView.mCardCheckButton.startAnimation(myAnim)
                    itemView.mCardCheckButton.setImageResource(R.mipmap.check_icon_clear)
                }
            }

            val viewPager = itemView.findViewById<ViewPager>(R.id.mCardImageList)
            val adapter = ImageAdapter(itemView.context,hillfort.imageStore)
            viewPager.adapter = adapter
            itemView.setOnClickListener { listener.onHillFortClick(hillfort) }
        }
    }
}