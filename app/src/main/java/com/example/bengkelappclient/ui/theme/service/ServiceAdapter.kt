// di dalam ui/adapter/ServiceAdapter.kt

package com.example.bengkelappclient.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bengkelappclient.R
import com.example.bengkelappclient.data.model.Service
import com.example.bengkelappclient.databinding.ItemServiceBinding

class ServiceAdapter : ListAdapter<Service, ServiceAdapter.ServiceViewHolder>(DIFF_CALLBACK) {

    private val BASE_IMAGE_URL = "http://10.0.2.2:8000/uploads/services/"
    private var listener: OnItemClickListener? = null

    // Interface untuk menangani klik
    interface OnItemClickListener {
        fun onUpdateClick(service: Service)
        fun onDeleteClick(service: Service)
    }

    // Fungsi untuk Activity/Fragment mendaftarkan diri sebagai listener
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        val binding = ItemServiceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ServiceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ServiceViewHolder(private val binding: ItemServiceBinding) : RecyclerView.ViewHolder(binding.root) {
        // Set listener di dalam init block dari ViewHolder
        init {
            binding.btnUpdate.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener?.onUpdateClick(getItem(position))
                }
            }

            binding.btnDelete.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener?.onDeleteClick(getItem(position))
                }
            }
        }

        fun bind(service: Service) {
            binding.tvName.text = service.name
            binding.tvPrice.text = "Rp${service.price}"
            val fullImageUrl = BASE_IMAGE_URL + service.img
            Glide.with(itemView.context)
                .load(fullImageUrl)
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.baseline_hide_image_24)
                .into(binding.ivImage)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Service>() {
            override fun areItemsTheSame(oldItem: Service, newItem: Service) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Service, newItem: Service) = oldItem == newItem
        }
    }
}