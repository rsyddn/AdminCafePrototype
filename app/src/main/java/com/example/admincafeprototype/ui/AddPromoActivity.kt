package com.example.admincafeprototype.ui

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.admincafeprototype.R
import com.example.admincafeprototype.util.CalendarHelper
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_add_promo.*
import kotlinx.android.synthetic.main.template_toolbar.*
import java.text.SimpleDateFormat
import java.util.*

class AddPromoActivity : AppCompatActivity() {
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_promo)
        setup()
    }

    private fun setup() {
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        var cal = Calendar.getInstance()

        val promoExpDate = SimpleDateFormat("dd/MM/yyyy").format(System.currentTimeMillis())
        val promoCreDate = SimpleDateFormat("dd/MM/yyyy").format(System.currentTimeMillis())

        val dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val myFormat = "dd/MM/yyyy" // mention the format you need
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                tEPromoExpDate.setText(sdf.format(cal.time))
            }

        val dateSetListener2 =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val myFormat = "dd/MM/yyyy" // mention the format you need
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                tEPromoCreDate.setText(sdf.format(cal.time))
            }
        tEPromoExpDate.setOnClickListener {

            val dialog = DatePickerDialog(
                this, dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            )

            dialog.datePicker.minDate = CalendarHelper.getCurrentDateInMills()
            dialog.datePicker.maxDate =
                CalendarHelper.getCurrentDateInMills() * (1000 * 60 * 60 * 24 * 7)
            dialog.show()
        }

        tEPromoCreDate.setOnClickListener {
            val dialog = DatePickerDialog(
                this, dateSetListener2,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            )
            dialog.datePicker.maxDate = CalendarHelper.getCurrentDateInMills()
            dialog.show()
        }


        val promoName = tEPromoName.text.toString()
        val promoCost = tEPromoCost.text.toString()
        val promoStock = tEPromoStock.text.toString()
        val promoDesc = tEPromoDesc.text.toString()
        buttonAddPromo.setOnClickListener {
            println(promoExpDate)
            addData(promoName, promoCost, promoStock, promoDesc, promoExpDate, promoCreDate)
        }

    }

    private fun addData(
        promoName: String,
        promoCost: String,
        promoStock: String,
        promoDesc: String,
        promoExpDate: String,
        promoCreDate: String
    ) {
    }
}
