package com.example.admincafeprototype.ui.activity.home

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.admincafeprototype.R
import com.example.admincafeprototype.ui.activity.scanqr.ScanQrActivity
import com.example.admincafeprototype.ui.fragment.promotion.PromotionFragment
import com.example.admincafeprototype.ui.fragment.transaction.TransactionFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.template_toolbar.*

class HomeActivity : AppCompatActivity() {
    private var mCurrentFragment: Fragment? = PromotionFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        if (savedInstanceState == null) {
            mCurrentFragment?.let {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.frameLayout, it)
                    .commit()
            }
        } else {
            mCurrentFragment = supportFragmentManager.getFragment(savedInstanceState, "FRAGMENT")
            mCurrentFragment?.let {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.frameLayout, it)
                    .commit()
            }
        }
        setUp()
    }

    private fun setUp() {
        setSupportActionBar(toolbar)
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.promotions -> {
                    mCurrentFragment = PromotionFragment()
                }
                R.id.transaction -> {
                    mCurrentFragment =
                        TransactionFragment()
                }
            }
            supportFragmentManager.beginTransaction()
                .replace(R.id.frameLayout, mCurrentFragment!!)
                .commit()
            true
        }
        qrScanButton.setOnClickListener {
            val moveIntent = Intent(this@HomeActivity, ScanQrActivity::class.java)
            startActivity(moveIntent)
        }
    }




}

