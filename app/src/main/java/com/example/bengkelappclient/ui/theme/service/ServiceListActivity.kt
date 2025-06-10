package com.example.bengkelappclient.ui.theme.service

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
import com.example.bengkelappclient.databinding.ActivityServiceListBinding
import com.example.bengkelappclient.ui.adapter.ServiceAdapter
import com.example.bengkelappclient.ui.service.ServiceViewModel
import com.example.bengkelappclient.ui.theme.admin.AddServiceActivity
import com.example.bengkelappclient.util.Resource // Pastikan import Resource
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class ServiceListActivity : AppCompatActivity(), ServiceAdapter.OnItemClickListener {

    private lateinit var binding: ActivityServiceListBinding
    private val viewModel: ServiceViewModel by viewModels()
    private lateinit var serviceAdapter: ServiceAdapter

    // Daftar ini digunakan untuk fungsi pencarian (search)
    private var fullServiceList: List<Service> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityServiceListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            finish()
        }

        setupRecyclerView()
        setupSearch()
        observeViewModel()
        // Tidak perlu memanggil fetchAllServices() di sini, karena sudah dipanggil di init ViewModel
    }

    private fun setupRecyclerView() {
        serviceAdapter = ServiceAdapter()
        serviceAdapter.setOnItemClickListener(this)
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

    // DIUBAH: Menggabungkan semua observer ke dalam satu fungsi
    private fun observeViewModel() {
        // Mengamati daftar layanan
        viewModel.serviceList.observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.recyclerView.visibility = View.GONE
                }
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val services = resource.data
                    if (services.isNullOrEmpty()) {
                        binding.recyclerView.visibility = View.GONE
                        // Tampilkan pesan kosong jika perlu
                    } else {
                        binding.recyclerView.visibility = View.VISIBLE
                        fullServiceList = services
                        serviceAdapter.submitList(fullServiceList)
                    }
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, "Gagal memuat: ${resource.message}", Toast.LENGTH_LONG).show()
                }
            }
        }

        // Mengamati hasil dari operasi hapus/tambah/update
        viewModel.operationResult.observe(this) { event ->
            event.getContentIfNotHandled()?.let { resource ->
                when (resource) {
                    is Resource.Loading -> { /* Tampilkan loading jika perlu */ }
                    is Resource.Success -> {
                        Toast.makeText(this, resource.data, Toast.LENGTH_SHORT).show()
                        // Daftar akan di-refresh secara otomatis dari ViewModel
                    }
                    is Resource.Error -> {
                        Toast.makeText(this, "Error: ${resource.message}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    override fun onUpdateClick(service: Service) {
        val intent = Intent(this@ServiceListActivity, AddServiceActivity::class.java)
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