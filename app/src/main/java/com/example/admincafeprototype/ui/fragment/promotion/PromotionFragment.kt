package com.example.admincafeprototype.ui.fragment.promotion

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.admincafeprototype.R
import com.example.admincafeprototype.model.Promo
import com.example.admincafeprototype.ui.activity.promo.AddPromoActivity
import com.example.admincafeprototype.ui.activity.promo.DetailPromoActivity
import com.example.admincafeprototype.ui.adapter.promotion.PromotionAdapter
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_promotion.*


class PromotionFragment : Fragment() {
    private val firestore = FirebaseFirestore.getInstance()
    val list = mutableListOf<Promo>()
    private lateinit var pListAdapter : PromotionAdapter
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == 1){
            if (resultCode == Activity.RESULT_OK){
                //refresh
//                refresh()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun add() {
        buttonPromotionAdd.setOnClickListener { view ->
            val intent = Intent(getActivity(), AddPromoActivity::class.java)
            getActivity()?.startActivity(intent)
        }
    }

    private fun show() {
        pListAdapter = PromotionAdapter(
            onClickListener = { promo ->
                val intent = DetailPromoActivity.newIntent(requireContext())
                intent.putExtra(DetailPromoActivity.PROMO_KEY, promo)
                startActivityForResult(intent, 1)
            }
        )
        recyclerPromotion.apply {
            adapter = pListAdapter
            layoutManager = LinearLayoutManager(this.context)
        }
        refresh()
    }

    private fun refresh(){
        firestore.collection("promos")
            .get().addOnSuccessListener { documents ->
                documents.forEach() {
                    list.add(it.toObject(Promo::class.java))
                }
                pListAdapter.addList(list)
            }
    }
}
