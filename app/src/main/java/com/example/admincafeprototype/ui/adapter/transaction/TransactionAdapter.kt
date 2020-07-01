package com.example.admincafeprototype.ui.adapter.transaction

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.admincafeprototype.R
import com.example.admincafeprototype.model.Claimed
import com.example.admincafeprototype.model.Promo
import kotlinx.android.synthetic.main.card_promotion_list.view.*
import kotlinx.android.synthetic.main.card_transaction_list.view.*
import java.text.SimpleDateFormat
import java.util.*

class TransactionAdapter(
    private val claimedList: MutableList<Claimed> = mutableListOf(),
    private val onClickListener: (Claimed) -> Unit
) : RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    fun addList(list: List<Claimed>?) {
        claimedList.clear()
        list?.let { claimed ->

            claimedList.addAll(claimed)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TransactionAdapter.TransactionViewHolder {
        return TransactionViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.card_transaction_list, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return claimedList.size
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val claimed: Claimed = claimedList[position]
        holder.bind(claimed)
    }

    inner class TransactionViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(claimed: Claimed) {
            val pattern = " dd-MM-yyyy HH:mm:ss"
            val parserPattern = "EEE MMM dd HH:mm:ss zzz yyyy"
            val toFormat = SimpleDateFormat(pattern, Locale.getDefault())
            val parser = SimpleDateFormat(parserPattern, Locale.getDefault())

            val claimedDate = parser.parse(claimed.claimedDate.toString())
            val szClaimedDate = claimedDate?.let {
                toFormat.format(it)
            } ?: ""
            view.txtTransactionId.text = claimed.claimedId
            view.txtClaimedDate.text = szClaimedDate
            view.setOnClickListener{
                onClickListener(claimed)
            }
        }

    }
}