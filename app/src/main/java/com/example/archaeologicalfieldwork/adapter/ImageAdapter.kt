package com.example.archaeologicalfieldwork.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.example.archaeologicalfieldwork.helper.readImageFromPath
import com.example.archaeologicalfieldwork.models.Images

class ImageAdapter(private val mContext: Context, private val imageList: Images) : PagerAdapter() {
    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view === obj
    }

    override fun getCount(): Int {
        return 1
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val imageView = ImageView(mContext)
        imageView.scaleType = ImageView.ScaleType.FIT_CENTER
        Glide.with(mContext).load(imageList.image).into(imageView);
        container.addView(imageView,0)
        return imageView
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        container.removeView(obj as ImageView)
    }
}
