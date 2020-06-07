package com.example.admincafeprototype.ui

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.admincafeprototype.R
import com.example.admincafeprototype.model.Promo
import kotlinx.android.synthetic.main.activity_detail_promo.*
import kotlinx.android.synthetic.main.template_toolbar.*
import java.text.SimpleDateFormat
import java.util.*

class DetailPromoActivity : AppCompatActivity() {
    private lateinit var promo: Promo
    private var isEdited = false

    private val pattern = "dd/MM/yyyy"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_promo)
        setup()
        isEditableForm()
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
        val toFormat = SimpleDateFormat(pattern, Locale.getDefault())
        val parser = SimpleDateFormat(parserPattern, Locale.getDefault())

        val expDate = parser.parse(promo.promoExpDate.toString())
        val szExDate: String = toFormat.format(expDate!!)
        //Create Date
        val creDate = parser.parse(promo.promoCreateDate.toString())
        val szCrDate: String = toFormat.format(creDate!!)

        tEPromoNameD.setText(promo.promoName)
        tEPromoCreDateD.setText(szCrDate)
        tEPromoExpDateD.setText(szExDate)
        tEPromoCostD.setText(promo.promoCost.toString())
        tEPromoStockD.setText(promo.promoStock.toString())
        tEPromoDescD.setText(promo.promoDetail)

        textEditProperties(isEdited)

        buttonEditPromoD.setOnClickListener {
            isEdited = !isEdited
            Toast.makeText(this, "Edit : $isEdited", Toast.LENGTH_SHORT).show()
            textEditProperties(isEdited)
        }

    }

    private fun isEditableForm() {
            val cal = Calendar.getInstance()
            tEPromoCreDateD.setOnClickListener {
                Toast.makeText(this, "LOL Create", Toast.LENGTH_LONG).show()
                val promoExpListener =
                    DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                        cal.set(Calendar.YEAR, year)
                        cal.set(Calendar.MONTH, monthOfYear)
                        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                        val dateFormat = SimpleDateFormat(pattern, Locale.getDefault())
                        tEPromoCreDateD.setText(dateFormat.format(cal.time))
                    }

                val dialog = DatePickerDialog(
                    this, promoExpListener,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
                )
                dialog.show()
            }
            tEPromoExpDateD.setOnClickListener {
                Toast.makeText(this, "LOL Exp", Toast.LENGTH_LONG).show()
                val promoExpListener =
                    DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                        cal.set(Calendar.YEAR, year)
                        cal.set(Calendar.MONTH, monthOfYear)
                        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                        val dateFormat = SimpleDateFormat(pattern, Locale.getDefault())
                        tEPromoExpDateD.setText(dateFormat.format(cal.time))
                    }

                val dialog = DatePickerDialog(
                    this, promoExpListener,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
                )
                dialog.show()
            }
    }

    private fun textEditProperties(value: Boolean) {
        tEPromoNameD.isEnabled = value
        tEPromoCreDateD.isEnabled = value
        tEPromoExpDateD.isEnabled = value
        tEPromoCostD.isEnabled = value
        tEPromoStockD.isEnabled = value
        tEPromoDescD.isEnabled = value
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, DetailPromoActivity::class.java)
        }

        const val PROMO_KEY = "PROMO_KEY"
    }
}
