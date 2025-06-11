package com.example.bengkelappclient.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button // Pastikan ini diimport
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.bengkelappclient.R
import com.example.bengkelappclient.data.model.BookingDetails

// PASTIKAN KONSTRUKTOR ADAPTER MEMILIKI 'onUpdateStatusClick' SEBAGAI LAMBDA
class AdminOrderStatusAdapter(private val onUpdateStatusClick: (BookingDetails) -> Unit) : // <--- BARIS INI PENTING
    ListAdapter<BookingDetails, AdminOrderStatusAdapter.BookingViewHolder>(DIFF_CALLBACK) {

    private val BASE_IMAGE_URL = "http://10.0.2.2:8000/uploads/services/"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_order_status_admin, parent, false)
        return BookingViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookingViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class BookingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // ... (deklarasi TextView, LinearLayout seperti sebelumnya) ...
        private val tvOrderStatus: TextView = itemView.findViewById(R.id.tvOrderStatus)
        private val tvBookingDateHeader: TextView = itemView.findViewById(R.id.tvBookingDate)
        private val tvCustomerName: TextView = itemView.findViewById(R.id.tvCustomerName)
        private val tvCustomerPhone: TextView = itemView.findViewById(R.id.tvCustomerPhone)
        private val tvVehicleInfo: TextView = itemView.findViewById(R.id.tvVehicleInfo)
        private val tvLicensePlate: TextView = itemView.findViewById(R.id.tvLicensePlate)
        private val tvServiceType: TextView = itemView.findViewById(R.id.tvServiceType)
        private val tvComplaint: TextView = itemView.findViewById(R.id.tvComplaint)
        private val layoutComplaint: LinearLayout = itemView.findViewById(R.id.layoutComplaint)

        private val btnUpdateStatus: Button = itemView.findViewById(R.id.btnUpdateStatus) // <--- PASTIKAN ID TOMBOL BENAR

        init {
            // PASTIKAN LISTENER INI ADA DI INIT BLOCK VIEWHOLDER
            btnUpdateStatus.setOnClickListener { // <--- LISTENER UNTUK TOMBOL DI ITEM RECYCLERVIEW
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onUpdateStatusClick(getItem(position)) // <--- PEMANGGILAN LAMBDA INI PENTING
                }
            }
        }

        fun bind(item: BookingDetails) {
            // ... (logika bind seperti sebelumnya) ...
            tvOrderStatus.text = item.booking.status.uppercase()
            val formattedDateTime = "${item.booking.booking_date}, ${item.booking.booking_time.substring(0, 5)}"
            tvBookingDateHeader.text = formattedDateTime

            when (item.booking.status.lowercase()) {
                "pending" -> {
                    tvOrderStatus.setBackgroundResource(R.drawable.status_badge_background_pending)
                    tvOrderStatus.setTextColor(ContextCompat.getColor(itemView.context, R.color.status_pending_text))
                }
                "confirmed" -> {
                    tvOrderStatus.setBackgroundResource(R.drawable.status_badge_background_confirmed)
                    tvOrderStatus.setTextColor(ContextCompat.getColor(itemView.context, R.color.status_confirmed_text))
                }
                "in_progress" -> {
                    tvOrderStatus.setBackgroundResource(R.drawable.status_badge_background_in_progress)
                    tvOrderStatus.setTextColor(ContextCompat.getColor(itemView.context, R.color.status_in_progress_text))
                }
                "completed" -> {
                    tvOrderStatus.setBackgroundResource(R.drawable.status_badge_background_completed)
                    tvOrderStatus.setTextColor(ContextCompat.getColor(itemView.context, R.color.status_completed_text))
                }
                "cancelled" -> {
                    tvOrderStatus.setBackgroundResource(R.drawable.status_badge_background_cancelled)
                    tvOrderStatus.setTextColor(ContextCompat.getColor(itemView.context, R.color.status_cancelled_text))
                }
                else -> {
                    tvOrderStatus.setBackgroundResource(R.drawable.status_badge_background_default)
                    tvOrderStatus.setTextColor(ContextCompat.getColor(itemView.context, R.color.status_default_text))
                }
            }

            tvCustomerName.text = item.booking.user?.name ?: "Nama Tidak Tersedia"
            tvCustomerPhone.text = item.booking.user?.phone ?: "Nomor Tidak Tersedia"
            tvVehicleInfo.text = "${item.vehicle.brand} ${item.vehicle.name}"
            tvLicensePlate.text = item.vehicle.licensePlate
            tvServiceType.text = item.service?.name ?: "Layanan Tidak Dikenal"

            if (!item.booking.complaint.isNullOrBlank()) {
                layoutComplaint.visibility = View.VISIBLE
                tvComplaint.text = item.booking.complaint
            } else {
                layoutComplaint.visibility = View.GONE
            }
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