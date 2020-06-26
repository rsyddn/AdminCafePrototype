package com.example.admincafeprototype.ui.activity.home

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.admincafeprototype.R
import com.example.admincafeprototype.model.Claimed
import com.example.admincafeprototype.ui.fragment.promotion.PromotionFragment
import com.example.admincafeprototype.ui.fragment.transaction.TransactionFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.template_toolbar.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class HomeActivity : AppCompatActivity() {
    private val firestore = FirebaseFirestore.getInstance()
    private var mCurrentFragment: Fragment? = PromotionFragment()
    private val pattern = "yyyy-MM-dd HH:mm:ss"
    private val dateFormat = SimpleDateFormat(pattern, Locale.getDefault())
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
            IntentIntegrator(this)
                .setOrientationLocked(false)
                .setBeepEnabled(false)
                .initiateScan()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(resultCode, data)
        if (result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                firestore.collection("purchased").document(result.contents).get()
                    .addOnSuccessListener { document ->
                        if(document?.get("purchasedId") == result.contents){
                            addClaimed(result.contents)
                        }else{
                            Toast.makeText(this, "Wrong QR Code!!", Toast.LENGTH_LONG).show();
                        }
                    }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private fun addClaimed(res: String) {
        val claimedId = firestore.collection("claimed").document().id
        val purcahsedId = res

        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
        val formatted = current.format(formatter)

        val claimedDate = dateFormat.parse(formatted)

        val claimed = Claimed(
            claimedId,
            purcahsedId,
            claimedDate
        )

        firestore.collection("claimed").document(claimedId)
            .set(claimed)
            .addOnSuccessListener {
                firestore.collection("purchased").document(res)
                    .update("isClaimed", true).addOnSuccessListener {
                        Toast.makeText(
                            this.baseContext,
                            "Transaction is Successed!",
                            Toast.LENGTH_LONG
                        ).show()
                    }
            }

    }


}

