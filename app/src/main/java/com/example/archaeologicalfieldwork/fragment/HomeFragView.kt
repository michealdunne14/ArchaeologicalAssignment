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
import kotlinx.android.synthetic.main.fragment_home.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.intentFor

class HomeFragView : BaseFragmentView(),HillFortListener,AnkoLogger {

    lateinit var homeFragPresenter: HomeFragPresenter

    override fun onHillFortClick(hillfort: HillFortModel) {
        startActivityForResult(context?.intentFor<AddFortView>()?.putExtra("hillfort_edit", hillfort), 0)
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
        return view
    }

    override fun showHillforts(
        hillfort: List<HillFortModel>,
        user: UserModel,
        images: ArrayList<Images>
    ) {
        view?.mListRecyclerView?.adapter = HillFortAdapter(hillfort, this,homeFragPresenter,user,images)
        view?.mListRecyclerView?.adapter?.notifyDataSetChanged()
    }

}
