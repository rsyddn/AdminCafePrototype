package com.example.admincafeprototype.ui.activity.scanqr

import android.Manifest
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.admincafeprototype.R
import com.example.admincafeprototype.model.Claimed
import com.google.firebase.firestore.FirebaseFirestore
import com.google.zxing.Result
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_qr)
        Dexter.withActivity(this)
            .withPermission(Manifest.permission.CAMERA)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                    mScannerView.setResultHandler(this@ScanQrActivity)
                    mScannerView.startCamera()
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: PermissionRequest?,
                    p1: PermissionToken?
                ) {
                }
                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                    Toast.makeText(
                        this@ScanQrActivity,
                        "You should enable this permission",
                        Toast.LENGTH_LONG
                    ).show()
                    finish()
                }
            }).check()
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
        if (rawResult != null) {
            firestore.collection("purchased").document(rawResult.toString()).get()
                .addOnSuccessListener { document ->
                    if (document?.get("purchasedId") == rawResult.toString()) {
                        if (document?.get("isClaimed") != true) {
                            addClaimed(rawResult.toString())
                        } else {
                            Toast.makeText(this, "QR Code Already Used!!", Toast.LENGTH_LONG).show()
                            finish()
                        }
                    } else {
                        Toast.makeText(this, "Wrong QR Code!!", Toast.LENGTH_LONG).show()
                        finish()
                    }
                }
        } else {
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