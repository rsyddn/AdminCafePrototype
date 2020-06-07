package com.example.admincafeprototype.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.admincafeprototype.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bottom_sheet_auth.*

class MainActivity : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUp()
    }

    private fun setUp() {
        firebaseAuth = FirebaseAuth.getInstance()

        setContentView(R.layout.activity_main)

        btn_login.setOnClickListener { setUpBottom("Login") }
        btn_register.setOnClickListener { setUpBottom("Register") }
    }

    private fun setUpBottom(auth: String) {
        val bottomSheet = BottomSheetDialog(this)
        bottomSheet.apply {
            window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
            setContentView(R.layout.bottom_sheet_auth)
        }.show()

        //Get Button Text
        bottomSheet.mb_auth.text = auth
        //Change to another activity

        if (auth == "Register") {
            bottomSheet.tf_retype_password.visibility = View.VISIBLE
            bottomSheet.mb_auth.setOnClickListener {
                // Do Firebase Register
                val email = bottomSheet.te_email.text.toString()
                val pass = bottomSheet.te_password.text.toString()
                val rePass = bottomSheet.te_retype_password.text.toString()
                if (pass == rePass) {
                    // Firebase Register
                    register(email, pass)
                    bottomSheet.dismiss()
                }
            }
        } else {
            bottomSheet.mb_auth.setOnClickListener {
                val email = bottomSheet.te_email.text.toString()
                val pass = bottomSheet.te_password.text.toString()
                login(email, pass)
                bottomSheet.dismiss()
            }
        }
    }

    private fun login(email: String, pass: String) {
        firebaseAuth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    firestore.collection("users").document(firebaseAuth.uid!!).get()
                        .addOnSuccessListener { document ->
                            if (document?.get("role-type") == "admin") {
                                startActivity(Intent(this, HomeActivity::class.java))
                            } else if (document?.get("role-type") == "player") {
                                Toast.makeText(
                                    this,
                                    "This account can't use in here..",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                        }
                } else if (task.isComplete) {
                    Toast.makeText(this, "Login Failed!", Toast.LENGTH_SHORT)
                        .show()
                }

            }
    }

    private fun register(email: String, pass: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user =
                        hashMapOf(
                            "user-id" to firebaseAuth.currentUser?.uid,
                            "role-type" to "admin"
                        )
                    firestore.collection("users")
                        .document(firebaseAuth.currentUser?.uid.toString())
                        .set(user)
                        .addOnSuccessListener {
                            startActivity(Intent(this, MainActivity::class.java))
                            Toast.makeText(this, "Register is Success!!", Toast.LENGTH_SHORT)
                                .show()
                        }.addOnFailureListener {
                            Toast.makeText(this, "Register Failed!", Toast.LENGTH_SHORT)
                                .show()
                        }

                } else {
                    Toast.makeText(this, "Register Failed!", Toast.LENGTH_SHORT)
                        .show()
                }
            }
    }


}
