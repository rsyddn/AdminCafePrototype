package com.example.admincafeprototype.ui.fragment.promotion

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.admincafeprototype.R
import com.example.admincafeprototype.model.Promo
import com.example.admincafeprototype.ui.AddPromoActivity
import com.example.admincafeprototype.ui.DetailPromoActivity
import com.example.admincafeprototype.ui.adapter.promotion.PromotionAdapter
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_promotion.*


class PromotionFragment : Fragment() {
    private val firestore = FirebaseFirestore.getInstance()
    val list = mutableListOf<Promo>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_promotion, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup()
    }

    fun setup() {
        show()
        add()
    }

    private fun add() {
        buttonPromotionAdd.setOnClickListener { view ->
            val intent = Intent(getActivity(), AddPromoActivity::class.java)
            getActivity()?.startActivity(intent)
        }
    }

    private fun show() {
        val pListAdapter = PromotionAdapter(
            onClickListener = { promo ->
                val intent = DetailPromoActivity.newIntent(requireContext())
                intent.putExtra(DetailPromoActivity.PROMO_KEY, promo)
                startActivity(intent)
            }
        )
        recyclerPromotion.apply {
            adapter = pListAdapter
            layoutManager = LinearLayoutManager(this.context)
        }
        firestore.collection("promos")
            .get().addOnSuccessListener { documents ->
                documents.forEach() {
                    list.add(it.toObject(Promo::class.java))
                }
                pListAdapter.addList(list)
            }
    }
}
