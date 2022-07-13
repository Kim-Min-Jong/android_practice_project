package com.udemy.a7minutesworkout

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.udemy.a7minutesworkout.databinding.ItemHistoryRowBinding

class HistoryAdapter(private val items: ArrayList<String>): RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    class ViewHolder(binding: ItemHistoryRowBinding) : RecyclerView.ViewHolder(binding.root){
        val llHistoryItemMain = binding.llHistoryItemMain
        val tvItem = binding.tvItem
        val tvPosition = binding.tvPosition
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemHistoryRowBinding.inflate(LayoutInflater.from(parent.context), parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.itemView.context
        val date = items[position]
        holder.tvPosition.text = (position+1).toString()
        holder.tvItem.text = date.toString()

        if(position % 2 ==0){
            holder.llHistoryItemMain.setBackgroundColor(ContextCompat.getColor(context,R.color.light_grey))
        }else{
            holder.llHistoryItemMain.setBackgroundColor(ContextCompat.getColor(context,R.color.white))
        }

    }

    override fun getItemCount(): Int {
        return items.size
    }
}