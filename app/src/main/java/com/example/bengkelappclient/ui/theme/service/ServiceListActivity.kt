package com.example.bengkelappclient.ui.service

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bengkelappclient.data.model.Service
import com.example.bengkelappclient.data.model.ServiceResult
import com.example.bengkelappclient.databinding.ActivityServiceListBinding
import com.example.bengkelappclient.ui.adapter.ServiceAdapter
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
// Langkah 1: Tambahkan implementasi interface OnItemClickListener
class ServiceListActivity : AppCompatActivity(), ServiceAdapter.OnItemClickListener {

    private lateinit var binding: ActivityServiceListBinding
    private val viewModel: ServiceViewModel by viewModels()
    private lateinit var serviceAdapter: ServiceAdapter

    private var fullServiceList: List<Service> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityServiceListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupSearch()
        observeServices()
        observeDeleteResult() // Langkah 2: Panggil observer untuk delete
        viewModel.fetchAllServices()
    }

    private fun setupRecyclerView() {
        serviceAdapter = ServiceAdapter()
        serviceAdapter.setOnItemClickListener(this) // Langkah 3: Atur listener untuk adapter
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@ServiceListActivity)
            adapter = serviceAdapter
        }
    }

    private fun setupSearch() {
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filter(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun filter(text: String) {
        val filteredList = ArrayList<Service>()
        val query = text.lowercase(Locale.getDefault()).trim()

        if (query.isNotEmpty()) {
            for (service in fullServiceList) {
                if (service.name.lowercase(Locale.getDefault()).contains(query)) {
                    filteredList.add(service)
                }
            }
            serviceAdapter.submitList(filteredList)
        } else {
            serviceAdapter.submitList(fullServiceList)
        }
    }

    private fun observeServices() {
        viewModel.allServices.observe(this) { result ->
            when (result) {
                is ServiceResult.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.emptyState.visibility = View.GONE
                }
                is ServiceResult.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val services = result.data
                    if (services.isNullOrEmpty()) {
                        binding.emptyState.visibility = View.VISIBLE
                        binding.recyclerView.visibility = View.GONE
                    } else {
                        binding.emptyState.visibility = View.GONE
                        binding.recyclerView.visibility = View.VISIBLE
                        fullServiceList = services
                        serviceAdapter.submitList(fullServiceList)
                    }
                }
                is ServiceResult.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.emptyState.visibility = View.VISIBLE
                    Toast.makeText(
                        this,
                        "Gagal memuat layanan: ${result.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun observeDeleteResult() {
        viewModel.deleteResult.observe(this) { result ->
            when(result) {
                is ServiceResult.Loading -> { /* Tampilkan loading jika perlu */ }
                is ServiceResult.Success -> {
                    Toast.makeText(this, result.data, Toast.LENGTH_SHORT).show()
                }
                is ServiceResult.Error -> {
                    Toast.makeText(this, "Error: ${result.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    // Fungsi dari interface sekarang akan dianggap sebagai override yang valid
    override fun onUpdateClick(service: Service) {
        val intent = Intent(this@ServiceListActivity, AddServiceActivity::class.java)
        // Kirim seluruh objek 'service' ke AddServiceActivity
        intent.putExtra(AddServiceActivity.EXTRA_SERVICE, service)
        startActivity(intent)
    }

    override fun onDeleteClick(service: Service) {
        AlertDialog.Builder(this)
            .setTitle("Konfirmasi Hapus")
            .setMessage("Apakah Anda yakin ingin menghapus '${service.name}'?")
            .setPositiveButton("Ya") { _, _ ->
                viewModel.deleteService(service.id)
            }
            .setNegativeButton("Tidak", null)
            .show()
    }
}
