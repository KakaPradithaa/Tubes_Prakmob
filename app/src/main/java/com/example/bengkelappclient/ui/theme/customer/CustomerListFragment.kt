// ui/customer/CustomerListFragment.kt
package com.example.bengkelappclient.ui.theme.customer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.bengkelappclient.R
import com.example.bengkelappclient.databinding.FragmentCustomerBinding
import com.example.bengkelappclient.util.Resource
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CustomerListFragment : Fragment() {

    private var _binding: FragmentCustomerBinding? = null
    private val binding get() = _binding!! // Properti ini hanya valid antara onCreateView dan onDestroyView.

    private val viewModel: CustomerViewModel by viewModels()
    private lateinit var customerAdapter: CustomerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCustomerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupObservers()
        setupListeners()
    }

    private fun setupRecyclerView() {
        customerAdapter = CustomerAdapter { customer ->
            // Handle klik item customer, misal tampilkan detail atau edit
            Toast.makeText(requireContext(), "Clicked: ${customer.name}", Toast.LENGTH_SHORT).show()
        }
        binding.rvCustomers.adapter = customerAdapter
    }

    private fun setupObservers() {
        viewModel.customers.observe(viewLifecycleOwner) { customers ->
            customerAdapter.submitList(customers)
        }

        viewModel.networkState.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        binding.progressBarCustomers.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding.progressBarCustomers.visibility = View.GONE
                        // Data berhasil di-refresh dari API dan disimpan ke Room
                    }
                    is Resource.Error -> {
                        binding.progressBarCustomers.visibility = View.GONE
                        Toast.makeText(requireContext(), resource.message ?: "Error", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        viewModel.addCustomerResult.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        // Mungkin tampilkan loading di dialog tambah customer
                    }
                    is Resource.Success -> {
                        Toast.makeText(requireContext(), "Customer ${resource.data?.name} berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                        // Data di RecyclerView akan otomatis update karena observasi LiveData dari Room
                    }
                    is Resource.Error -> {
                        Toast.makeText(requireContext(), resource.message ?: "Gagal menambah customer", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun setupListeners() {
        binding.btnAddCustomer.setOnClickListener {
            showAddCustomerDialog()
        }
    }

    private fun showAddCustomerDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_customer, null)
        val etName = dialogView.findViewById<EditText>(R.id.etDialogCustomerName)
        val etPhone = dialogView.findViewById<EditText>(R.id.etDialogCustomerPhone)
        val etAddress = dialogView.findViewById<EditText>(R.id.etDialogCustomerAddress)

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Tambah Customer Baru")
            .setView(dialogView)
            .setPositiveButton("Tambah") { dialog, _ ->
                val name = etName.text.toString().trim()
                val phone = etPhone.text.toString().trim()
                val address = etAddress.text.toString().trim().ifEmpty { null }

                if (name.isNotEmpty() && phone.isNotEmpty()) {
                    viewModel.addCustomer(name, phone, address)
                } else {
                    Toast.makeText(requireContext(), "Nama dan Telepon tidak boleh kosong", Toast.LENGTH_SHORT).show()
                }
                dialog.dismiss()
            }
            .setNegativeButton("Batal") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Membersihkan referensi binding untuk menghindari memory leak
    }
}