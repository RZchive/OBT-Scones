package com.example.obt_scones.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.obt_scones.R
import com.example.obt_scones.model.TenagaKerjaModel

class TenagaKerjaAdapter(
    private val list: List<TenagaKerjaModel>,
    private val onItemClick: (TenagaKerjaModel) -> Unit
) : RecyclerView.Adapter<TenagaKerjaAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNama: TextView = itemView.findViewById(R.id.tvNamaPeserta)
        val tvNik: TextView = itemView.findViewById(R.id.tvNikPeserta)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_peserta, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.tvNama.text = item.nama ?: "Tanpa Nama"
        holder.tvNik.text = "NIK: ${item.nik ?: "-"}"
        holder.itemView.setOnClickListener { onItemClick(item) }
    }

    override fun getItemCount(): Int = list.size
}
