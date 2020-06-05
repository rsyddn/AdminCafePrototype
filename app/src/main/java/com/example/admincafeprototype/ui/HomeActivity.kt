package com.example.admincafeprototype.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.admincafeprototype.R
import com.example.admincafeprototype.ui.fragment.promotion.PromotionFragment
import com.example.admincafeprototype.ui.fragment.qr.ScanQrFragment
import com.example.admincafeprototype.ui.fragment.setting.SettingFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.template_toolbar.*

class HomeActivity : AppCompatActivity() {
    private var mCurrentFragment: Fragment? = ScanQrFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.frameLayout, mCurrentFragment!!)
                .commit()
        } else {
            mCurrentFragment = supportFragmentManager.getFragment(savedInstanceState, "FRAGMENT")
            supportFragmentManager.beginTransaction()
                .replace(R.id.frameLayout, mCurrentFragment!!)
                .commit()
        }

        setUp()
    }

    private fun setUp() {
        setSupportActionBar(toolbar)
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {

                R.id.qrScanner -> {
                    mCurrentFragment = ScanQrFragment()
                }
                R.id.promotions -> {
                    mCurrentFragment = PromotionFragment()
                }
                R.id.settings -> {
                    mCurrentFragment = SettingFragment()
                }
            }

            supportFragmentManager.beginTransaction()
                .replace(R.id.frameLayout, mCurrentFragment!!)
                .commit()
            true
        }
    }
}

