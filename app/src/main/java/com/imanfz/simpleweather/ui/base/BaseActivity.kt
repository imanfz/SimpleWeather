package com.imanfz.simpleweather.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.ethanhua.skeleton.Skeleton
import com.ethanhua.skeleton.SkeletonScreen
import com.imanfz.simpleweather.R
import com.imanfz.simpleweather.utils.hide
import com.imanfz.simpleweather.utils.show
import org.jetbrains.anko.find

abstract class BaseActivity<B: ViewBinding>(val bindingFactory : (LayoutInflater)->B)
    : AppCompatActivity() {

    val binding : B by lazy {
        bindingFactory(layoutInflater)
    }

    private var skeletonList: SkeletonScreen? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun setupToolbar(
        title: String,
        isBackEnabled: Boolean = true,
        isShareEnabled: Boolean = false
    ) {
        with(binding) {
            val tvTitle = root.find<AppCompatTextView>(R.id.tv_title)
            val btnBack = root.find<AppCompatImageView>(R.id.btn_back)
            val btnShare = root.find<AppCompatImageView>(R.id.btn_share)

            // set ui
            tvTitle.text = title

            btnBack.apply {
                if (isBackEnabled) {
                    show()
                    setOnClickListener { onBackPressed() }
                } else {
                    hide()
                }
            }

            btnShare.apply {
                if (isShareEnabled) {
                    show()
                } else {
                    hide()
                }
            }
        }
    }

    fun showSkeletonList(
        view: View, layout: Int,
        rvAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>? = null
    ) {
        if (view is RecyclerView){
            skeletonList = Skeleton.bind(view)
                .adapter(rvAdapter)
                .count(6)
                .load(layout)
                .show()
        }
    }

    fun hideSkeletonList(){
        skeletonList?.hide()
    }
}