package com.main.netwallet.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.main.netwallet.R
import com.main.netwallet.database.IncomeTransaction
import androidx.recyclerview.widget.ListAdapter
import com.main.netwallet.database.ExpensesTransaction

class ShowExpensesAdapter : RecyclerView.Adapter<ShowExpensesAdapter.myHolder>() {

    var data = listOf<ExpensesTransaction>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myHolder {
        val v = LayoutInflater.from(parent.context)
        val view = v.inflate(R.layout.show_expenses, parent, false)
        return myHolder(view)
    }

    override fun onBindViewHolder(holder: myHolder, position: Int) {
        val item = data[position]
        val res = holder.itemView.context.resources
        holder.value.text = item.value.toString()
        holder.transactionType.text = item.transactionType.toString()
        holder.transactionDetails.text = item.details.toString()
    }

    override fun getItemCount(): Int = data?.size

    class myHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
//            itemView.findViewById<TextView>(R.id.valueList).text = get.value.toString()
//            itemView.findViewById<TextView>(R.id.transactionTypeList).text = get.transactionType.toString()
//            itemView.findViewById<TextView>(R.id.transactionDetailsList).text = get.details.toString()

        val value = itemView.findViewById<TextView>(R.id.valueList)
        val transactionType = itemView.findViewById<TextView>(R.id.transactionTypeList)
        val transactionDetails = itemView.findViewById<TextView>(R.id.transactionDetailsList)
    }
}
