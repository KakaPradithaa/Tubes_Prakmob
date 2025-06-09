package com.example.bengkelappclient.ui.adapter

import android.util.Log // Import Log untuk debugging
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bengkelappclient.R
import com.example.bengkelappclient.data.model.BookingDetails
import com.google.android.material.card.MaterialCardView

class OrderStatusAdapter : ListAdapter<BookingDetails, OrderStatusAdapter.BookingViewHolder>(DIFF_CALLBACK) {

    // BASE_IMAGE_URL INI SEKARANG SAMA PERSIS DENGAN YANG BERHASIL DI ServiceAdapter.kt
    // Pastikan ini benar-benar cocok dengan BASE_IMAGE_URL yang berfungsi di adapter lain.
    private val BASE_IMAGE_URL = "http://10.0.2.2:8000/uploads/services/"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_order_status, parent, false)
        return BookingViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookingViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class BookingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvServiceType: TextView = itemView.findViewById(R.id.tvServiceType)
        private val tvBookingDate: TextView = itemView.findViewById(R.id.tvBookingDate)
        private val tvBookingTime: TextView = itemView.findViewById(R.id.tvBookingTime)
        private val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
        private val cardStatusBackground: MaterialCardView = itemView.findViewById(R.id.cardStatusBackground)
        private val tvBrand: TextView = itemView.findViewById(R.id.tvBrand)
        private val tvModel: TextView = itemView.findViewById(R.id.tvModel)
        private val tvLicensePlate: TextView = itemView.findViewById(R.id.tvLicensePlate)
        private val tvComplaint: TextView = itemView.findViewById(R.id.tvComplaint)
        private val ivServiceIcon: ImageView = itemView.findViewById(R.id.ivServiceIcon)

        fun bind(item: BookingDetails) { // 'item' adalah objek BookingDetails yang sedang di-bind
            tvServiceType.text = item.service?.name ?: "Layanan Tidak Dikenal"
            tvBookingDate.text = item.booking.booking_date
            tvBookingTime.text = item.booking.booking_time
            tvStatus.text = item.booking.status

            when (item.booking.status.lowercase()) { // Gunakan lowercase untuk memastikan kecocokan
                "pending" -> {
                    // Sesuaikan warna untuk status "pending"
                    cardStatusBackground.setCardBackgroundColor(ContextCompat.getColor(itemView.context, R.color.status_pending_bg))
                    tvStatus.setTextColor(ContextCompat.getColor(itemView.context, R.color.status_pending_text))
                }
                "confirmed" -> {
                    // Sesuaikan warna untuk status "confirmed" (mungkin sama dengan "proses" atau warna baru)
                    cardStatusBackground.setCardBackgroundColor(ContextCompat.getColor(itemView.context, R.color.status_confirmed_bg))
                    tvStatus.setTextColor(ContextCompat.getColor(itemView.context, R.color.status_confirmed_text))
                }
                "in_progress" -> { // Ini sama dengan "proses" yang sebelumnya
                    cardStatusBackground.setCardBackgroundColor(ContextCompat.getColor(itemView.context, R.color.orange_light)) // Ganti dengan nama warna yang lebih generik jika mau
                    tvStatus.setTextColor(ContextCompat.getColor(itemView.context, R.color.orange_dark)) // Ganti dengan nama warna yang lebih generik jika mau
                }
                "completed" -> { // Ini sama dengan "selesai" yang sebelumnya
                    cardStatusBackground.setCardBackgroundColor(ContextCompat.getColor(itemView.context, R.color.green_dark))
                    tvStatus.setTextColor(ContextCompat.getColor(itemView.context, R.color.green_light))
                }
                "cancelled" -> { // Ini sama dengan "dibatalkan" yang sebelumnya
                    cardStatusBackground.setCardBackgroundColor(ContextCompat.getColor(itemView.context, R.color.red_light))
                    tvStatus.setTextColor(ContextCompat.getColor(itemView.context, R.color.red_dark))
                }
                else -> {
                    // Fallback jika ada status lain yang tidak terduga dari backend
                    cardStatusBackground.setCardBackgroundColor(ContextCompat.getColor(itemView.context, R.color.grey_light))
                    tvStatus.setTextColor(ContextCompat.getColor(itemView.context, R.color.grey_dark))
                }
            }

            tvBrand.text = item.vehicle.brand
            tvModel.text = item.vehicle.name
            Log.d("OrderStatusAdapter", "Nama Model Kendaraan: ${item.vehicle.name}")
            tvLicensePlate.text = item.vehicle.licensePlate
            tvComplaint.text = item.booking.complaint ?: "Tidak ada keluhan"

            // LOGIKA PEMUATAN GAMBAR
            // Menggunakan '!!' berarti Anda sangat yakin item.service dan item.service.img tidak null.
            // Hati-hati karena ini bisa menyebabkan NullPointerException jika data tidak sesuai ekspektasi.
            val fullImageUrl = BASE_IMAGE_URL + item.service!!.img

            // Log untuk debugging. Ini akan menampilkan URL yang coba dimuat oleh Glide.
            Log.d("OrderStatusAdapter", "Mencoba memuat gambar dari URL: $fullImageUrl")

            // Memuat gambar menggunakan Glide
            Glide.with(itemView.context)
                .load(fullImageUrl) // Memberikan URL lengkap ke Glide
                .placeholder(R.drawable.ic_placeholder) // Tampilkan gambar placeholder saat memuat
                .error(R.drawable.engineoil) // <--- TAMBAHKAN KEMBALI INI! Sangat penting untuk debugging.
                .into(ivServiceIcon) // Memasukkan gambar ke ImageView ivServiceIcon
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<BookingDetails>() {
            override fun areItemsTheSame(oldItem: BookingDetails, newItem: BookingDetails): Boolean =
                oldItem.booking.id == newItem.booking.id

            override fun areContentsTheSame(oldItem: BookingDetails, newItem: BookingDetails): Boolean =
                oldItem == newItem
        }
    }
}