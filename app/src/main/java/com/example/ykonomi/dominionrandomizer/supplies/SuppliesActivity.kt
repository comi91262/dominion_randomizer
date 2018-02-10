package com.example.ykonomi.dominionrandomizer.supplies

import android.os.Bundle
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import com.example.ykonomi.dominionrandomizer.R
import android.support.design.widget.NavigationView

class SuppliesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.list)


        // Set up the navigation drawer.
        val dl: DrawerLayout = findViewById(R.id.drawer_layout)
        //dl.setStatusBarBackground(R.color.colorPrimaryDark)

        val nv: NavigationView = findViewById(R.id.nav_view)

        nv.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId){
                R.id.list_config_menu_item -> {
                    // Do nothing, we're already on that screen
                }
            }
            // Close the navigation drawer when an item is selected.
            menuItem.isChecked = true
            dl.closeDrawers()

            true
        }

    }
}
