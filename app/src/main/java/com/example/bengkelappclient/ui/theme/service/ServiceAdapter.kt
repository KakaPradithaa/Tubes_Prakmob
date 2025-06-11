package com.example.bengkelappclient.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bengkelappclient.R
import com.example.bengkelappclient.data.model.Service
import com.example.bengkelappclient.databinding.ItemServiceBinding
import java.text.NumberFormat
import java.util.Locale

class ServiceAdapter : ListAdapter<Service, ServiceAdapter.ServiceViewHolder>(DIFF_CALLBACK) {

    private var listener: OnItemClickListener? = null

    companion object {
        private const val BASE_URL = "http://10.0.2.2:8000/storage/"

        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Service>() {
            override fun areItemsTheSame(oldItem: Service, newItem: Service) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Service, newItem: Service) = oldItem == newItem
        }
    }

    interface OnItemClickListener {
        fun onUpdateClick(service: Service)
        fun onDeleteClick(service: Service)
    }

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

            // Format harga dalam Rupiah
            try {
                val priceAsDouble = service.price.toDouble()
                val localeID = Locale("in", "ID")
                val numberFormat = NumberFormat.getCurrencyInstance(localeID)
                numberFormat.maximumFractionDigits = 0
                binding.tvPrice.text = numberFormat.format(priceAsDouble)
            } catch (e: NumberFormatException) {
                binding.tvPrice.text = "Rp ${service.price}"
            }

            // Menangani gambar dari URL
            val imagePath = service.img
            if (!imagePath.isNullOrEmpty()) {
                val fullImageUrl = BASE_URL + imagePath
                // Log.d("ServiceAdapter", "Mencoba memuat gambar dari URL: $fullImageUrl")

                Glide.with(itemView.context)
                    .load(fullImageUrl)
                    .placeholder(R.drawable.ic_placeholder) // atau R.drawable.placeholder_solid
                    .error(R.drawable.baseline_hide_image_24) // atau R.drawable.placeholder_solid
                    .into(binding.ivImage)
            } else {
                binding.ivImage.setImageResource(R.drawable.ic_placeholder) // atau placeholder_solid
            }
        }
    }
}
