package com.example.admincafeprototype.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.admincafeprototype.R
import com.example.admincafeprototype.ui.activity.home.HomeActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SplashScreenActivity : AppCompatActivity() {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        if (firebaseAuth.currentUser == null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
            firestore.collection("users").document(firebaseAuth.uid!!).get()
                .addOnSuccessListener { document ->
                    if (document?.get("role-type") == "admin"
                    ) {
                        startActivity(Intent(this, HomeActivity::class.java))
                    }
                    finish()
                }.addOnFailureListener {
                    Toast.makeText(this, "Login Failed!", Toast.LENGTH_SHORT)
                        .show()
                }
        }
    }
}
