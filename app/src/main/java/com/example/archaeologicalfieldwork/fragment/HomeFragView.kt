package com.example.archaeologicalfieldwork.fragment

import android.annotation.SuppressLint
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
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import org.jetbrains.anko.*

class HomeFragView : BaseFragmentView(),HillFortListener,AnkoLogger {

    lateinit var homeFragPresenter: HomeFragPresenter

    override fun onHillFortClick(
        hillfort: HillFortModel,
        images: ArrayList<Images>
    ) {
        val stringList = ArrayList<String>()
        for (i in images){
            if (i.hillfortFbid == hillfort.fbId) {
                stringList.add(i.image)
            }
        }
        homeFragPresenter.findNotes(hillfort.fbId)
        startActivityForResult(context?.intentFor<AddFortView>()?.putExtra("hillfort_edit", hillfort)?.putExtra("images",stringList), 0)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val layoutManager = LinearLayoutManager(context)
        info { "Home fragment started" }
        homeFragPresenter = initPresenter(HomeFragPresenter(this)) as HomeFragPresenter
        val view = inflater.inflate(R.layout.fragment_home, container, false)


        view.mListRecyclerView.layoutManager = layoutManager as RecyclerView.LayoutManager?
        homeFragPresenter.findallHillforts()

        view.mFloatingCancelButton.setOnClickListener {
            homeFragPresenter.clearHillforts()
            homeFragPresenter.findallHillforts()
        }

        return view
    }

    @SuppressLint("RestrictedApi")
    override fun showFloatingAction() {
        mFloatingCancelButton.visibility = View.VISIBLE
    }

    @SuppressLint("RestrictedApi")
    override fun hideFloatingAction() {
        mFloatingCancelButton.visibility = View.GONE
    }

    override fun showHillforts(
        hillfort: List<HillFortModel>,
        user: UserModel,
        images: ArrayList<Images>
    ) {
        mListRecyclerView.adapter = HillFortAdapter(hillfort, this, homeFragPresenter, images, user)
        mListRecyclerView.adapter?.notifyDataSetChanged()
    }

}
