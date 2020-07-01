package com.example.admincafeprototype.ui.activity.scanqr

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.admincafeprototype.R
import com.example.admincafeprototype.model.Claimed
import com.google.firebase.firestore.FirebaseFirestore
import com.google.zxing.Result
import kotlinx.android.synthetic.main.activity_scan_qr.*
import me.dm7.barcodescanner.zxing.ZXingScannerView
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class ScanQrActivity : AppCompatActivity(), ZXingScannerView.ResultHandler {
    private val firestore = FirebaseFirestore.getInstance()
    private val pattern = "yyyy-MM-dd HH:mm:ss"
    private val dateFormat = SimpleDateFormat(pattern, Locale.getDefault())

    private lateinit var mScannerView: ZXingScannerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_qr)
        initScannerView()
    }

    private fun initScannerView() {
        mScannerView = ZXingScannerView(this)
        mScannerView.setAutoFocus(true)
        mScannerView.setResultHandler(this)
        frame_layout_camera.addView(mScannerView)
    }

    override fun onStart() {
        super.onStart()
        mScannerView.startCamera()
    }

    override fun onPause() {
        super.onPause()
        mScannerView.stopCamera()
    }

    override fun onResume() {
        super.onResume()
        mScannerView.setResultHandler(this) // Register ourselves as a handler for scan results.
        mScannerView.startCamera()
    }

    override fun handleResult(rawResult: Result?) {
        if(rawResult != null){
            firestore.collection("purchased").document(rawResult.toString()).get()
                .addOnSuccessListener { document ->
                    if(document?.get("purchasedId") == rawResult.toString()){
                        if(document?.get("isClaimed") != true){
                            addClaimed(rawResult.toString())
                        }else{
                            Toast.makeText(this, "QR Code Already Used!!", Toast.LENGTH_LONG).show()
                            finish()
                        }
                    }else{
                        Toast.makeText(this, "Wrong QR Code!!", Toast.LENGTH_LONG).show()
                        finish()
                    }
                }
        }else{
            finish()
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
                        finish()
                    }
            }
    }
}