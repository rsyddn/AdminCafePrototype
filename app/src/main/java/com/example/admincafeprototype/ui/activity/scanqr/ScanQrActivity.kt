package com.example.admincafeprototype.ui.activity.scanqr

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.admincafeprototype.R
import com.example.admincafeprototype.model.Claimed
import com.example.admincafeprototype.ui.activity.home.HomeActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.zxing.integration.android.IntentIntegrator
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class ScanQrActivity : AppCompatActivity() {
    private val firestore = FirebaseFirestore.getInstance()
    private val pattern = "yyyy-MM-dd HH:mm:ss"
    private val dateFormat = SimpleDateFormat(pattern, Locale.getDefault())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_qr)
        IntentIntegrator(this)
            .setOrientationLocked(false)
            .setBeepEnabled(false)
            .initiateScan()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(resultCode, data)
        if (result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
                val moveIntent = Intent(this@ScanQrActivity, HomeActivity::class.java)
                startActivity(moveIntent)
            } else {
                firestore.collection("purchased").document(result.contents).get()
                    .addOnSuccessListener { document ->
                        if(document?.get("purchasedId") == result.contents){
                            if(document?.get("isClaimed") != true){
                                addClaimed(result.contents)
                            }else{
                                Toast.makeText(this, "QR Code Already Used!!", Toast.LENGTH_LONG).show()
                                val moveIntent = Intent(this@ScanQrActivity, HomeActivity::class.java)
                                startActivity(moveIntent)
                            }
                        }else{
                            Toast.makeText(this, "Wrong QR Code!!", Toast.LENGTH_LONG).show()
                            val moveIntent = Intent(this@ScanQrActivity, HomeActivity::class.java)
                            startActivity(moveIntent)

                        }
                    }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
            val moveIntent = Intent(this@ScanQrActivity, HomeActivity::class.java)
            startActivity(moveIntent)
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
                        val moveIntent = Intent(this@ScanQrActivity, HomeActivity::class.java)
                        startActivity(moveIntent)
                    }
            }

    }
}