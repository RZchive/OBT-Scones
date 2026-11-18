package com.example.obt_scones.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.obt_scones.R
import com.example.obt_scones.model.TrainingModel

class TrainingAdapter(
    private val trainingList: List<TrainingModel>,
    private val onItemClick: (TrainingModel) -> Unit
) : RecyclerView.Adapter<TrainingAdapter.TrainingViewHolder>() {

    class TrainingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tvTrainingTitle)
        val tvCategory: TextView = itemView.findViewById(R.id.tvTrainingCategory)
        val tvQuota: TextView = itemView.findViewById(R.id.tvTrainingQuota)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_training, parent, false)
        return TrainingViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrainingViewHolder, position: Int) {
        val item = trainingList[position]
        holder.tvTitle.text = item.title
        holder.tvCategory.text = item.category
        holder.tvQuota.text = "Kuota: ${item.quota}"
        holder.itemView.setOnClickListener { onItemClick(item) }
    }

    override fun getItemCount(): Int = trainingList.size
}
