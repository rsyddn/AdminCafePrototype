package com.example.admincafeprototype.ui.adapter.promotion

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.admincafeprototype.R
import com.example.admincafeprototype.model.Promo
import kotlinx.android.synthetic.main.card_promotion_list.view.*

class PromotionAdapter (
    private val promoList: MutableList<Promo> = mutableListOf()
) : RecyclerView.Adapter<PromotionAdapter.PromoViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PromotionAdapter.PromoViewHolder {
        return PromoViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.card_promotion_list, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return promoList.size
    }

    override fun onBindViewHolder(holder: PromotionAdapter.PromoViewHolder, position: Int) {
        val promo : Promo = promoList[position]
        holder.bind(promo)
        addList(null)
    }

    fun addList(list: List<Promo>?) {
        list?.let { promo ->
            promoList.addAll(promo)
            notifyDataSetChanged()
        }
    }

    inner class PromoViewHolder(private val view : View) :RecyclerView.ViewHolder(view){
        fun bind(promotion: Promo){
            view.txtPromoName.text = promotion.promoName
            view.txtPromoCreateDate.text = promotion.promoCreateDate.toString()
            view.txtPromoExpDate.text = promotion.promoExpDate.toString()
        }
    }
}