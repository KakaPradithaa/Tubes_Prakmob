package com.example.bengkelappclient.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bengkelappclient.R
import com.example.bengkelappclient.data.model.Service // Pastikan model Service Anda di sini

class ServiceAdapter : ListAdapter<Service, ServiceAdapter.ServiceViewHolder>(DIFF_CALLBACK) {

    // DEFINE BASE_IMAGE_URL DI SINI
    // GANTI "http://your-backend-api.com/path/to/images/" dengan BASE URL ASLI GAMBAR ANDA
    // Contoh: "https://api.bengkelapp.com/storage/images/" atau "http://192.168.1.100:8000/storage/"
    private val BASE_IMAGE_URL = "http://10.0.2.2:8000/uploads/services/" // <--- INI PENTING! GANTI DENGAN URL SEBENARNYA!


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_service, parent, false)
        return ServiceViewHolder(view)
    }

    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ServiceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(service: Service) {
            itemView.findViewById<TextView>(R.id.tvName).text = service.name
            itemView.findViewById<TextView>(R.id.tvPrice).text = "Rp${service.price}"

            // BANGUN URL LENGKAP DI SINI
            val fullImageUrl = BASE_IMAGE_URL + service.img
            // Log untuk debugging (opsional, hapus setelah berhasil)
            // Log.d("ServiceAdapter", "Loading image from: $fullImageUrl")

            Glide.with(itemView.context)
                .load(fullImageUrl) // <--- UBAH KE fullImageUrl
                .placeholder(R.drawable.ic_placeholder)
                .into(itemView.findViewById(R.id.ivImage))
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Service>() {
            override fun areItemsTheSame(oldItem: Service, newItem: Service): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Service, newItem: Service): Boolean =
                oldItem == newItem
        }
    }
}