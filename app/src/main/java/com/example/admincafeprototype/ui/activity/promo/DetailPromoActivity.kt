package com.example.admincafeprototype.ui.activity.promo

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.admincafeprototype.R
import com.example.admincafeprototype.model.Promo
import kotlinx.android.synthetic.main.activity_detail_promo.*
import kotlinx.android.synthetic.main.template_toolbar.*
import java.text.SimpleDateFormat
import java.util.*

class DetailPromoActivity : AppCompatActivity() {
    private lateinit var promo: Promo
    private val pattern = "dd/MM/yyyy"
    private val dateFormat = SimpleDateFormat(pattern, Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_promo)
        setup()
//        isEditableForm()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_edit, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == 1){
            if(resultCode == Activity.RESULT_OK){
                setResult(Activity.RESULT_OK)
                finish()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when(item.itemId){
            android.R.id.home -> {
                finish()
                true
            }
            R.id.menu_edit -> {
                val i =
                    EditPromoActivity.newIntent(
                        this,
                        promo
                    )
                startActivityForResult(i, 1)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setup() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (intent.hasExtra(PROMO_KEY)) {
            promo = intent.getParcelableExtra(PROMO_KEY) as Promo
        } else {
            finish()
        }
        val parserPattern = "EEE MMM dd HH:mm:ss zzz yyyy"
        val parser = SimpleDateFormat(parserPattern, Locale.getDefault())
        //Expired Date
        val expDate = parser.parse(promo.promoExpDate.toString())
        val szExDate: String = dateFormat.format(expDate!!)
        //Create Date
        val creDate = parser.parse(promo.promoCreateDate.toString())
        val szCrDate: String = dateFormat.format(creDate!!)

        txtPromoNameD.text = promo.promoName
        txtPromoCreDateD.text = szCrDate
        txtPromoExpDateD.text = szExDate
        txtPromoCostD.text = promo.promoCost.toString()
        txtPromoStockD.text = promo.promoStock.toString()
        txtPromoDescD.text = promo.promoDetail

    }
    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, DetailPromoActivity::class.java)
        }
        const val PROMO_KEY = "PROMO_KEY"
    }
}
