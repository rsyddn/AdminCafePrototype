package com.example.admincafeprototype.ui.adapter.promotion

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.admincafeprototype.R
import com.example.admincafeprototype.model.Promo
import kotlinx.android.synthetic.main.card_promotion_list.view.*
import java.text.SimpleDateFormat
import java.util.*

class PromotionAdapter(
    private val promoList: MutableList<Promo> = mutableListOf() ,
    private val onClickListener: (Promo) -> Unit
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
        val promo: Promo = promoList[position]
        holder.bind(promo)
    }

    fun addList(list: List<Promo>?) {
        promoList.clear()
        list?.let { promo ->

            promoList.addAll(promo)
        }
        notifyDataSetChanged()
    }

    inner class PromoViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(promotion: Promo) {
            //Expired Date

            val pattern = "dd/MM/yyyy"
            val parserPattern = "EEE MMM dd HH:mm:ss zzz yyyy"
            val toFormat = SimpleDateFormat(pattern, Locale.getDefault())
            val parser = SimpleDateFormat(parserPattern, Locale.getDefault())

            val expDate = parser.parse(promotion.promoExpDate.toString())
            val szExDate: String = toFormat.format(expDate!!)
            //Create Date
            val creDate = parser.parse(promotion.promoCreateDate.toString())
            val szCrDate: String = toFormat.format(creDate!!)
            view.txtPromoName.text = promotion.promoName
            view.txtPromoDesc.text = promotion.promoDetail!!.take(100)
            view.txtPromoCreateDate.text = szCrDate
            view.txtPromoExpDate.text = szExDate
            view.setOnClickListener{
                onClickListener(promotion)
            }
        }
    }
}