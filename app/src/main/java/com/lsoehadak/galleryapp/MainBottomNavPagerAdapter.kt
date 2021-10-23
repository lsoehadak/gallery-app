package com.lsoehadak.galleryapp

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.lsoehadak.galleryapp.home.HomeFragment
import com.lsoehadak.galleryapp.saved.SavedImageFragment

class MainBottomNavPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private var pageCount: Int
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return pageCount
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HomeFragment()
            else -> SavedImageFragment()
        }
    }

}