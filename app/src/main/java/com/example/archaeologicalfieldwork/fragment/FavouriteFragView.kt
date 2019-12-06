package com.example.archaeologicalfieldwork.fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
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
import com.example.archaeologicalfieldwork.models.UserModel
import kotlinx.android.synthetic.main.fragment_home.view.*
import org.jetbrains.anko.info
import org.jetbrains.anko.intentFor


class FavouriteFragView : BaseFragmentView(), HillFortListener {

    lateinit var favouriteFragPresenter: FavouriteFragPresenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layoutManager = LinearLayoutManager(context)
        info { "Favourite fragment started" }
        favouriteFragPresenter = initPresenter(FavouriteFragPresenter(this)) as FavouriteFragPresenter
        val view = inflater.inflate(R.layout.fragment_favourite, container, false)


        view.mListRecyclerView.layoutManager = layoutManager as RecyclerView.LayoutManager?
        favouriteFragPresenter.findallHillforts()
        return view
    }

    override fun showHillforts(hillfort: List<HillFortModel>, user: UserModel) {
        view?.mListRecyclerView?.adapter = HillFortAdapter(hillfort, this,favouriteFragPresenter,user)
        view?.mListRecyclerView?.adapter?.notifyDataSetChanged()
    }

    override fun onHillFortClick(hillfort: HillFortModel) {
        startActivityForResult(context?.intentFor<AddFortView>()?.putExtra("hillfort_edit", hillfort), 0)
    }

}
