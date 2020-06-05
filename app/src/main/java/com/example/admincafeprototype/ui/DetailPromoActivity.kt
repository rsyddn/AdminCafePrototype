package com.example.admincafeprototype.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.admincafeprototype.R
import com.example.admincafeprototype.model.Promo
import kotlinx.android.synthetic.main.activity_detail_promo.*
import kotlinx.android.synthetic.main.template_toolbar.*
import java.text.SimpleDateFormat
import java.util.*

class DetailPromoActivity : AppCompatActivity() {
    private lateinit var promo : Promo
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_promo)
        setup()
    }

    private fun setup() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if(intent.hasExtra(PROMO_KEY)){
            promo = intent.getParcelableExtra(PROMO_KEY) as Promo
        }else{
            finish()
        }

        val pattern = "dd/MM/yyyy"
        val parserPattern = "EEE MMM dd HH:mm:ss zzz yyyy"
        val toFormat = SimpleDateFormat(pattern, Locale.getDefault())
        val parser = SimpleDateFormat(parserPattern, Locale.getDefault())

        val expDate = parser.parse(promo.promoExpDate.toString())
        val szExDate: String = toFormat.format(expDate)
        //Create Date
        val creDate = parser.parse(promo.promoCreateDate.toString())
        val szCrDate: String = toFormat.format(creDate)

        tEPromoNameD.setText(promo.promoName)
        tEPromoCreDateD.setText(szCrDate)
        tEPromoExpDateD.setText(szExDate)
        promo.promoCost?.let{ tEPromoCostD.setText(it) }
        promo.promoStock?.let { tEPromoStockD.setText(it)}
        tEPromoDescD.setText(promo.promoDetail)

    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, DetailPromoActivity::class.java)
        }
        const val PROMO_KEY = "PROMO_KEY"
    }
}
