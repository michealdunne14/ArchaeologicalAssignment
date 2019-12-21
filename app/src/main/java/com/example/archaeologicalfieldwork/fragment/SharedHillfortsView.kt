package com.example.archaeologicalfieldwork.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.archaeologicalfieldwork.R
import com.example.archaeologicalfieldwork.activities.AddFort.AddFortView
import com.example.archaeologicalfieldwork.activities.BaseFragment.BaseFragmentView
import com.example.archaeologicalfieldwork.adapter.HillFortAdapter
import com.example.archaeologicalfieldwork.adapter.HillFortListener
import com.example.archaeologicalfieldwork.models.HillFortModel
import com.example.archaeologicalfieldwork.models.Images
import com.example.archaeologicalfieldwork.models.UserModel
import kotlinx.android.synthetic.main.fragment_shared_hillforts.*
import kotlinx.android.synthetic.main.fragment_shared_hillforts.view.*
import org.jetbrains.anko.intentFor

class SharedHillfortsView : BaseFragmentView(), HillFortListener {

    lateinit var sharedHillfortsPresenter: SharedHillfortsPresenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val layoutManager = LinearLayoutManager(context)
        val view = inflater.inflate(R.layout.fragment_shared_hillforts, container, false)
        sharedHillfortsPresenter = initPresenter(SharedHillfortsPresenter(this)) as SharedHillfortsPresenter
        view.mShareHillforts.layoutManager = layoutManager as RecyclerView.LayoutManager?

        sharedHillfortsPresenter.findSharedHillforts()
        return view
    }

    override fun showHillforts(
        hillfort: List<HillFortModel>,
        user: UserModel,
        images: ArrayList<Images>
    ) {
        mShareHillforts.adapter = HillFortAdapter(hillfort, this,sharedHillfortsPresenter,images,user)
        mShareHillforts.adapter?.notifyDataSetChanged()
    }

    override fun onHillFortClick(hillfort: HillFortModel, images: ArrayList<Images>) {
        val stringList = ArrayList<String>()
        for (i in images){
            if (i.hillfortFbid == hillfort.fbId) {
                stringList.add(i.image)
            }
        }
        startActivityForResult(context?.intentFor<AddFortView>()?.putExtra("hillfort_edit", hillfort), 0)

    }

}
