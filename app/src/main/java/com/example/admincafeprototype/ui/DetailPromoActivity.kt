package com.example.admincafeprototype.ui

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.admincafeprototype.R
import com.example.admincafeprototype.model.Promo
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_add_promo.*
import kotlinx.android.synthetic.main.activity_detail_promo.*
import kotlinx.android.synthetic.main.template_toolbar.*
import java.text.SimpleDateFormat
import java.util.*

class DetailPromoActivity : AppCompatActivity() {
    private lateinit var promo: Promo
    private var isEdited = false
    private val firestore = FirebaseFirestore.getInstance()
    private val pattern = "dd/MM/yyyy"
    private val dateFormat = SimpleDateFormat(pattern, Locale.getDefault())

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
        val parser = SimpleDateFormat(parserPattern, Locale.getDefault())
        val expDate = parser.parse(promo.promoExpDate.toString())
        val szExDate: String = dateFormat.format(expDate!!)
        //Create Date
        val creDate = parser.parse(promo.promoCreateDate.toString())
        val szCrDate: String = dateFormat.format(creDate!!)

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
            buttonDeletePromoD.visibility = View.VISIBLE
        }

        buttonDeletePromoD.setOnClickListener {
            firestore.collection("promos").document(promo.promoId!!)
                .delete()
                .addOnSuccessListener {
                    Toast.makeText(this, "Data is Deleted", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener{
                    Toast.makeText(this, "Data is Failed to deleted", Toast.LENGTH_SHORT).show()
                }
        }

    }

    private fun isEditableForm() {
        val promoNameD = tEPromoNameD.text.toString()
        val promoCostD = tEPromoCostD.text.toString()
        val promoStockD = tEPromoStockD.text.toString()
        val promoDescD = tEPromoDescD.text.toString()

        val cal = Calendar.getInstance()
        tEPromoCreDateD.setOnClickListener {
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
        //Expired Date
        val expDate = tEPromoExpDateD.text.toString()
        val promoExpDateD = dateFormat.parse(expDate)
        //Create Date
        val creDate = tEPromoCreDateD.text.toString()
        val promoCreDateD = dateFormat.parse(creDate)
        val promoIdD = promo.promoId
        val promoIsActiveD = true

        val update =
            mapOf(
                "promoId" to promo.promoId,
                "promoName" to promoNameD,
                "promoCreateDate" to promoCreDateD,
                "promoExpDate" to promoExpDateD,
                "promoCost" to promoCostD.toInt(),
                "promoStock" to promoStockD.toInt(),
                "promoDetail" to promoDescD,
                "promoIsActive" to promoIsActiveD
            )

        firestore.collection("promos").document(promo.promoId!!)
            .update(update)
            .addOnSuccessListener {
                Toast.makeText(this, "Data is Edited", Toast.LENGTH_SHORT).show()
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
