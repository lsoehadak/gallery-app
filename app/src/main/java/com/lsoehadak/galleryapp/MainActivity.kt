package com.lsoehadak.galleryapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lsoehadak.galleryapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var activityMainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        activityMainBinding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.m_home -> {
                    activityMainBinding.viewPager.currentItem = 0
                    return@setOnItemSelectedListener true
                }
                else -> {
                    activityMainBinding.viewPager.currentItem = 1
                    return@setOnItemSelectedListener true
                }
            }
        }

        with(activityMainBinding.viewPager) {
            val pagerAdapter = MainBottomNavPagerAdapter(supportFragmentManager, lifecycle, 2)
            adapter = pagerAdapter
            offscreenPageLimit = 2
            isUserInputEnabled = false
        }
    }
}