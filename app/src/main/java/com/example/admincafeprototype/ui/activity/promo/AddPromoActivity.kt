package com.example.admincafeprototype.ui.activity.promo

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.admincafeprototype.R
import com.example.admincafeprototype.model.Promo
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

        val cal = Calendar.getInstance()

        tEPromoExpDate.setOnClickListener {
            val PromoExpListener =
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    cal.set(Calendar.YEAR, year)
                    cal.set(Calendar.MONTH, monthOfYear)
                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    tEPromoExpDate.setText(dateFormat.format(cal.time))
                }

            val dialog = DatePickerDialog(
                this, PromoExpListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            )
            dialog.show()
        }

        tEPromoCreDate.setOnClickListener {
            val PromoCreListener =
                DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                    cal.set(Calendar.YEAR, year)
                    cal.set(Calendar.MONTH, monthOfYear)
                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    tEPromoCreDate.setText(dateFormat.format(cal.time))
                }

            val dialog = DatePickerDialog(
                this, PromoCreListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            )
            dialog.show()
        }

        buttonAddPromo.setOnClickListener {
            if(IsInputValid()){
                Input()
            }else{
                when{
                    tEPromoName.text.toString().isNullOrEmpty() ->tEPromoName.setError("Name Cannot Empty")
                    tEPromoCreDate.text.toString().isNullOrEmpty() ->tEPromoCreDate.setError("Created Date Cannot Empty")
                    tEPromoExpDate.text.toString().isNullOrEmpty() ->tEPromoExpDate.setError("Expired Date Cannot Empty")
                    tEPromoCost.text.toString().isNullOrEmpty() ->tEPromoCost.setError("Cost Cannot Empty")
                    tEPromoStock.text.toString().isNullOrEmpty() ->tEPromoStock.setError("Stock Cannot Empty")
                    tEPromoDesc.text.toString().isNullOrEmpty() ->tEPromoDesc.setError("Description Cannot Empty")
                }
            }
        }

    }

    private fun Input() {
        val promoName = tEPromoName.text.toString()
        val promoCost = tEPromoCost.text.toString()
        val promoStock = tEPromoStock.text.toString()
        val promoDesc = tEPromoDesc.text.toString()

        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        var expDate = tEPromoExpDate.text.toString()
        val creDate = tEPromoCreDate.text.toString()
        val promoCreDate = dateFormat.parse(creDate)
        val promoExpDate = dateFormat.parse(expDate)
        val promoId = firestore.collection("promos").document().id
        val promoIsActive = true
        val promo = Promo(
            promoId,
            promoName,
            promoDesc,
            promoStock.toInt(),
            promoCost.toInt(),
            promoCreDate,
            promoExpDate,
            promoIsActive
        )
        firestore.collection("promos").document(promoId)
            .set(promo)
            .addOnSuccessListener {
                Toast.makeText(this, "Data is Added", Toast.LENGTH_SHORT).show()
                finish()
            }
    }
    private fun IsInputValid() :Boolean{
        return  when{
            tEPromoName.text.toString().isNullOrEmpty() ->false
            tEPromoCost.text.toString().isNullOrEmpty() ->false
            tEPromoStock.text.toString().isNullOrEmpty() ->false
            tEPromoDesc.text.toString().isNullOrEmpty() ->false
            tEPromoExpDate.text.toString().isNullOrEmpty() ->false
            tEPromoCreDate.text.toString().isNullOrEmpty() ->false
            else -> true
        }
    }


}
