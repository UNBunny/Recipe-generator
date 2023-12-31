package com.example.recipegenerator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.room.Room
import com.example.recipegenerator.database.AppDatabase
import com.example.recipegenerator.ui.book.BookFragment
import com.example.recipegenerator.ui.home.HomeFragment
import com.example.recipegenerator.ui.setting.SettingFragment
import com.qamar.curvedbottomnaviagtion.CurvedBottomNavigation

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val bottomNavigationView = findViewById<CurvedBottomNavigation>(R.id.bottomNavigation)
        bottomNavigationView.add(
            (CurvedBottomNavigation.Model(
                1,
                "",
                R.drawable.baseline_menu_book_24
            ))
        )
        bottomNavigationView.add(
            (CurvedBottomNavigation.Model(
                2,
                "",
                R.drawable.baseline_soup_kitchen_24
            ))
        )
        bottomNavigationView.add(
            (CurvedBottomNavigation.Model(
                3,
                "",
                R.drawable.baseline_settings
            ))
        )

        bottomNavigationView.setOnClickMenuListener {
            when (it.id) {
                1 -> {
                    replaceFragment(BookFragment())
                }

                2 -> {
                    replaceFragment(HomeFragment())
                }

                3 -> {
                    replaceFragment(SettingFragment())
                }
            }
        }
        replaceFragment(HomeFragment())
        bottomNavigationView.show(2)
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}