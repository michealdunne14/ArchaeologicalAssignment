package com.example.archaeologicalfieldwork.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.archaeologicalfieldwork.R
import com.example.archaeologicalfieldwork.activities.AddFortActivity
import com.example.archaeologicalfieldwork.adapter.HillFortAdapter
import com.example.archaeologicalfieldwork.adapter.HillFortListener
import com.example.archaeologicalfieldwork.main.MainApp
import com.example.archaeologicalfieldwork.models.HillFortModel
import com.example.archaeologicalfieldwork.models.UserModel
import kotlinx.android.synthetic.main.fragment_home.view.*
import org.jetbrains.anko.intentFor

class HomeFragment : Fragment(),HillFortListener {
    override fun onHillFortClick(hillfort: HillFortModel) {
        startActivityForResult(context?.intentFor<AddFortActivity>()?.putExtra("hillfort_edit", hillfort), 0)
    }

    var user = UserModel()
    lateinit var app : MainApp

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val layoutManager = LinearLayoutManager(context)

        val view = inflater.inflate(R.layout.fragment_home, container, false)
        app = activity?.application as MainApp
        user = app.user


        view.mListRecyclerView.layoutManager = layoutManager as RecyclerView.LayoutManager?
        loadHillforts(view,user)
        return view
    }

    fun loadHillforts(view: View,userModel: UserModel) {
        showHillforts(app.users.findAllHillforts(userModel),view,userModel)
    }

    fun showHillforts (hillforts: List<HillFortModel>,view: View,userModel: UserModel) {
        view.mListRecyclerView.adapter = HillFortAdapter(hillforts, this,app,userModel)
        view.mListRecyclerView.adapter?.notifyDataSetChanged()
    }

}
