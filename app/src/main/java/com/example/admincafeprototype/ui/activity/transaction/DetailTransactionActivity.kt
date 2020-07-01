package com.example.admincafeprototype.ui.activity.transaction

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.admincafeprototype.R
import com.example.admincafeprototype.model.Claimed
import com.example.admincafeprototype.model.Promo
import com.example.admincafeprototype.model.Purchased
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_detail_transaction.*
import kotlinx.android.synthetic.main.template_toolbar.*
import java.text.SimpleDateFormat
import java.util.*

class DetailTransactionActivity : AppCompatActivity() {
    private val firestore = FirebaseFirestore.getInstance()
    private lateinit var claimed: Claimed
    private val pattern = "dd-MM-yyyy HH:mm:ss"
    private val dateFormat = SimpleDateFormat(pattern, Locale.getDefault())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_transaction)
        setup()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                setResult(Activity.RESULT_OK)
                finish()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setup() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (intent.hasExtra(CLAIMED_KEY)) {
            claimed = intent.getParcelableExtra(CLAIMED_KEY) as Claimed
        } else {
            finish()
        }

        val parserPattern = "EEE MMM dd HH:mm:ss zzz yyyy"
        val parser = SimpleDateFormat(parserPattern, Locale.getDefault())

        val claimedDate = parser.parse(claimed.claimedDate.toString())
        val szClaimedDate: String = claimedDate?.let {
            dateFormat.format(it)
        } ?: ""


        firestore.collection("purchased").document(claimed.purcahsedId!!)
        .get().addOnSuccessListener { documentSnapshot ->
            val purchased = documentSnapshot.toObject(Purchased::class.java)
            firestore.collection("promos").document(purchased?.promoId!!)
            .get().addOnSuccessListener {documentSnapshot ->
                val promos = documentSnapshot.toObject(Promo::class.java)
                    txtPromoClaimedName.text = promos?.promoName
                    txtPromoClaimedDetail.text = promos?.promoDetail
            }
        }

        txtClaimedId.text = claimed.claimedId
        txtClaimedDate.text = szClaimedDate
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, DetailTransactionActivity::class.java)
        }

        const val CLAIMED_KEY = "CLAIMED_KEY"
    }
}