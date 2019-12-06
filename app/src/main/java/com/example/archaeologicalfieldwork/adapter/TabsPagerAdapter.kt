package com.example.archaeologicalfieldwork.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.archaeologicalfieldwork.activities.Main.MainView
import com.example.archaeologicalfieldwork.fragment.FavouriteFragView
import com.example.archaeologicalfieldwork.fragment.HomeFragView
import com.example.archaeologicalfieldwork.fragment.SettingsFragView

class TabsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> {
                return SettingsFragView()
            }
            1 -> {
                return HomeFragView()
            }
            2 -> {
                return FavouriteFragView()
            }
            else -> return HomeFragView()
        }
    }

    override fun getCount(): Int {
        return 3
    }


    override fun getPageTitle(position: Int): CharSequence? {
        when (position) {
            0 -> return "Home"
            1 -> return "Settings"
            2 -> return "Favourite"
            else -> return null
        }
    }

}