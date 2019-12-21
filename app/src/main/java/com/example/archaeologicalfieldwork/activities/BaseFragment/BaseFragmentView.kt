package com.example.archaeologicalfieldwork.activities.BaseFragment

import android.content.Intent
import android.os.Parcelable
import androidx.fragment.app.Fragment
import com.example.archaeologicalfieldwork.activities.AddFort.AddFortView
import com.example.archaeologicalfieldwork.activities.BaseActivity.VIEW
import com.example.archaeologicalfieldwork.activities.EditLocation.EditLocationView
import com.example.archaeologicalfieldwork.activities.Login.LoginView
import com.example.archaeologicalfieldwork.activities.Main.MainView
import com.example.archaeologicalfieldwork.activities.Maps.HillfortMapsView
import com.example.archaeologicalfieldwork.activities.StartActivity
import com.example.archaeologicalfieldwork.models.HillFortModel
import com.example.archaeologicalfieldwork.models.Images
import com.example.archaeologicalfieldwork.models.UserModel
import org.jetbrains.anko.AnkoLogger

open abstract class BaseFragmentView: Fragment(), AnkoLogger {
    var baseFragmentPresenter: BaseFragmentPresenter? = null

    fun initPresenter(presenter: BaseFragmentPresenter): BaseFragmentPresenter {
        baseFragmentPresenter = presenter
        return presenter
    }

    open fun showHillforts(
        hillfort: List<HillFortModel>,
        user: UserModel,
        images: ArrayList<Images>
    ) {}
    open fun doSetDetails(email: String,password: String,name: String) {}
    open fun showFloatingAction(){}
    open fun hideFloatingAction(){}
}