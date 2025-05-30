// ui/customer/CustomerAdapter.kt
package com.example.bengkelappclient.ui.theme.customer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.bengkelappclient.data.model.Customer
import com.example.bengkelappclient.databinding.ItemCustomerBinding

class CustomerAdapter(private val onItemClick: (Customer) -> Unit) :
    ListAdapter<Customer, CustomerAdapter.CustomerViewHolder>(CustomerDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerViewHolder {
        val binding = ItemCustomerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CustomerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CustomerViewHolder, position: Int) {
        val customer = getItem(position)
        holder.bind(customer)
        holder.itemView.setOnClickListener {
            onItemClick(customer)
        }
    }

    inner class CustomerViewHolder(private val binding: ItemCustomerBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(customer: Customer) {
            binding.tvCustomerNameItem.text = customer.name
            binding.tvCustomerPhoneItem.text = customer.phoneNumber
            binding.tvCustomerAddressItem.text = customer.address ?: "Alamat tidak tersedia"
        }
    }

    class CustomerDiffCallback : DiffUtil.ItemCallback<Customer>() {
        override fun areItemsTheSame(oldItem: Customer, newItem: Customer): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Customer, newItem: Customer): Boolean {
            return oldItem == newItem
        }
    }
}