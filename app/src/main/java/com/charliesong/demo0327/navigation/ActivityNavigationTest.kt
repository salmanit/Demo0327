package com.charliesong.demo0327.navigation

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.view.MenuItem
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.charliesong.demo0327.R
import com.charliesong.demo0327.base.BaseActivity
import kotlinx.android.synthetic.main.activity_navigation_test.*

class ActivityNavigationTest : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation_test)
        defaultSetTitle("Navigation Learn")

        findNavController(my_nav_host_fragment).apply {
            addOnNavigatedListener(listener)
        }

        nav_bottom.setOnNavigationItemSelectedListener(object : BottomNavigationView.OnNavigationItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                when (item.itemId) {
                    R.id.settings_fragment -> {
                        showToast("setting")
                    }
                    R.id.second_fragment -> {
                        showToast("url")
                    }
                }
                return true
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean = findNavController(my_nav_host_fragment).navigateUp()

    val listener = object : NavController.OnNavigatedListener {
        override fun onNavigated(controller: NavController, destination: NavDestination) {


            println("listener============${destination.id}===${destination.label}")
        }
    }

    override fun onDestroy() {
        findNavController(my_nav_host_fragment).apply {
            removeOnNavigatedListener(listener)
        }
        super.onDestroy()

    }
}