package com.yasin.workout

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.yasin.workout.databinding.ItemsHistoryBinding

class ItemAdapter(
    private val items: ArrayList<DateEntity>,
    private val deleteListener: (id: Int) -> Unit
) :
    RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    class ViewHolder(binding: ItemsHistoryBinding): RecyclerView.ViewHolder(binding.root){
        val llMain = binding.llMain
        val tvDate = binding.tvDate
        val tvID = binding.tvID
        val ivDelete = binding.ivDelete
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemsHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.itemView.context
        val item = items[position]

        holder.tvDate.text = item.date
        holder.tvID.text = item.id.toString()

        if(position % 2 == 0){
            holder.llMain.setBackgroundColor(ContextCompat.getColor(holder.itemView.context,
                R.color.light_pink))
        }
        else{
            holder.llMain.setBackgroundColor(ContextCompat.getColor(holder.itemView.context,
                R.color.white))
        }

        holder.ivDelete.setOnClickListener{
            deleteListener.invoke(item.id)
        }
    }
}