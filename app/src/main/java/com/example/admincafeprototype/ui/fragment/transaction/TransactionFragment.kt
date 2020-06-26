package com.example.admincafeprototype.ui.fragment.transaction

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.admincafeprototype.R
import com.example.admincafeprototype.model.Claimed
import com.example.admincafeprototype.model.Promo
import com.example.admincafeprototype.ui.adapter.promotion.PromotionAdapter
import com.example.admincafeprototype.ui.adapter.transaction.TransactionAdapter
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_promotion.*
import kotlinx.android.synthetic.main.fragment_transaction.*

class TransactionFragment : Fragment() {
    private val firestore = FirebaseFirestore.getInstance()
    val list = mutableListOf<Claimed>()
    private lateinit var tListAdapter : TransactionAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_transaction, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUp()
    }

    private fun setUp() {
        tListAdapter = TransactionAdapter()
        recyclerTransaction.apply {
            adapter = tListAdapter
            layoutManager = LinearLayoutManager(this.context)
        }
        refresh()
    }

    private fun refresh() {
        firestore.collection("claimed")
            .get().addOnSuccessListener { documents ->
                documents.forEach() {
                    list.add(it.toObject(Claimed::class.java))
                }
                tListAdapter.addList(list)
            }
    }
}