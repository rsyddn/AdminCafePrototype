package com.example.admincafeprototype.ui.activity.promo

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.admincafeprototype.R
import com.example.admincafeprototype.model.Promo
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_edit_promo.*
import kotlinx.android.synthetic.main.template_toolbar.*
import java.text.SimpleDateFormat
import java.util.*

class EditPromoActivity : AppCompatActivity() {
    private lateinit var promo: Promo
    private val firestore = FirebaseFirestore.getInstance()
    private val pattern = "dd/MM/yyyy"
    private val dateFormat = SimpleDateFormat(pattern, Locale.getDefault())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_promo)
        if (intent.hasExtra(PROMO_KEY)) {
            promo = intent.getParcelableExtra(PROMO_KEY) as Promo
        }
        setUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_save, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.menu_save -> {

                isEditableForm()
                true

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    private fun setUp() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val parserPattern = "EEE MMM dd HH:mm:ss zzz yyyy"
        val parser = SimpleDateFormat(parserPattern, Locale.getDefault())
        val expDate = parser.parse(promo.promoExpDate.toString())
        val szExDate = expDate?.let {
            dateFormat.format(it)
        } ?: ""

        //Create Date
        val creDate = parser.parse(promo.promoCreateDate.toString())

        val szCrDate = creDate?.let {
            dateFormat.format(it)
        } ?: ""

        tEPromoNameE.setText(promo.promoName)
        tEPromoCreDateE.setText(szCrDate)
        tEPromoExpDateE.setText(szExDate)
        tEPromoCostE.setText(promo.promoCost.toString())
        tEPromoStockE.setText(promo.promoStock.toString())
        tEPromoDescE.setText(promo.promoDetail)

        buttonDeletePromoE.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.apply {
                setMessage("Are you sure to delete?")
                setPositiveButton("Yes") { dialog, id ->
                    firestore.collection("promos").document(promo.promoId!!)
                        .update("promoIsActive",false)
                        .addOnSuccessListener {
                            Toast.makeText(this@EditPromoActivity, "Data is Deleted", Toast.LENGTH_SHORT).show()
                            setResult(Activity.RESULT_OK)
                            finish()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this@EditPromoActivity, "Data is Failed to deleted", Toast.LENGTH_SHORT).show()
                        }
                }
                    .setNegativeButton("No") { dialog, id ->
                        // Dismiss the dialog
                        dialog.dismiss()
                    }
            }

            val alert = builder.create()
            alert.show()

        }

    }


    private fun isEditableForm() {
        val promoNameD = tEPromoNameE.text.toString()
        val promoCostD = tEPromoCostE.text.toString()
        val promoStockD = tEPromoStockE.text.toString()
        val promoDescD = tEPromoDescE.text.toString()

        val cal = Calendar.getInstance()
        tEPromoCreDateE.setOnClickListener {
            val promoExpListener =
                DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                    cal.set(Calendar.YEAR, year)
                    cal.set(Calendar.MONTH, monthOfYear)
                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    val dateFormat = SimpleDateFormat(pattern, Locale.getDefault())
                    tEPromoCreDateE.setText(dateFormat.format(cal.time))
                }

            val dialog = DatePickerDialog(
                this, promoExpListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            )
            dialog.show()
        }
        tEPromoExpDateE.setOnClickListener {
            val promoExpListener =
                DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                    cal.set(Calendar.YEAR, year)
                    cal.set(Calendar.MONTH, monthOfYear)
                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    val dateFormat = SimpleDateFormat(pattern, Locale.getDefault())
                    tEPromoExpDateE.setText(dateFormat.format(cal.time))
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
        val expDate = tEPromoExpDateE.text.toString()
        val promoExpDateD = dateFormat.parse(expDate)
        //Create Date
        val creDate = tEPromoCreDateE.text.toString()
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
                setResult(Activity.RESULT_OK)
                finish()
            }

    }

    companion object {
        fun newIntent(context: Context, p: Promo): Intent {
            return Intent(context, EditPromoActivity::class.java).apply {
                putExtra(PROMO_KEY, p)
            }

        }

        const val PROMO_KEY = "PROMO_KEY"
    }
}