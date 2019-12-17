package com.example.archaeologicalfieldwork.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.archaeologicalfieldwork.R
import com.example.archaeologicalfieldwork.activities.AddFort.AddFortView
import com.example.archaeologicalfieldwork.activities.BaseFragment.BaseFragmentView
import com.example.archaeologicalfieldwork.adapter.HillFortListener
import com.example.archaeologicalfieldwork.models.HillFortModel
import com.example.archaeologicalfieldwork.models.Images
import com.example.archaeologicalfieldwork.models.UserModel
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

//        view.mFavouriteListView.dispat
//        view.mFavouriteListView.layoutManager = layoutManager as RecyclerView.LayoutManager?
        favouriteFragPresenter.findallHillforts()
        return view
    }

    override fun showHillforts(
        hillfort: List<HillFortModel>,
        user: UserModel,
        images: ArrayList<Images>
    ) {
//        view?.mFavouriteListView?.adapter = HillFortAdapter(hillfort, this,favouriteFragPresenter,user)
//        view?.mFavouriteListView?.adapter?.notifyDataSetChanged()
    }

    override fun onHillFortClick(hillfort: HillFortModel) {
        startActivityForResult(context?.intentFor<AddFortView>()?.putExtra("hillfort_edit", hillfort), 0)
    }

}
