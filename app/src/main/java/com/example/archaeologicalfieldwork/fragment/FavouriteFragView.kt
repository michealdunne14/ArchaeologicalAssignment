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
import kotlinx.android.synthetic.main.fragment_favourite.*
import kotlinx.android.synthetic.main.fragment_favourite.view.*
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

        view.mFavouriteListView.layoutManager = layoutManager as RecyclerView.LayoutManager?
        favouriteFragPresenter.findallHillforts()
        return view
    }

    override fun showHillforts(
        hillfort: List<HillFortModel>,
        user: UserModel,
        images: ArrayList<Images>
    ) {
        mFavouriteListView.adapter = HillFortAdapter(
            hillfort,
            this,
            favouriteFragPresenter,
            images,
            user
        )
        mFavouriteListView.adapter?.notifyDataSetChanged()
    }

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
        startActivityForResult(context?.intentFor<AddFortView>()?.putExtra("hillfort_edit", hillfort)?.putExtra("images",stringList), 0)
    }

}
