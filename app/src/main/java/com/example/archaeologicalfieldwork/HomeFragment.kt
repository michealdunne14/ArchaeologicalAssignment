package com.example.archaeologicalfieldwork

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.archaeologicalfieldwork.activities.AddFortActivity
import com.example.archaeologicalfieldwork.adapter.HillFortAdapter
import com.example.archaeologicalfieldwork.adapter.HillFortListener
import com.example.archaeologicalfieldwork.main.MainApp
import com.example.archaeologicalfieldwork.models.HillFortModel
import kotlinx.android.synthetic.main.fragment_home.view.*
import org.jetbrains.anko.intentFor

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class HomeFragment : Fragment(),HillFortListener {
    override fun onHillFortClick(hillfort: HillFortModel) {
        startActivityForResult(context?.intentFor<AddFortActivity>()?.putExtra("hillfort_edit", hillfort), 0)
    }

    lateinit var app : MainApp

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val layoutManager = LinearLayoutManager(context)

        val view = inflater.inflate(R.layout.fragment_home, container, false)
        app = activity?.application as MainApp

        view.mListRecyclerView.layoutManager = layoutManager as RecyclerView.LayoutManager?
        view.mListRecyclerView.adapter = HillFortAdapter(app.hillforts.findAll(),this,app)
        return view
    }

}
