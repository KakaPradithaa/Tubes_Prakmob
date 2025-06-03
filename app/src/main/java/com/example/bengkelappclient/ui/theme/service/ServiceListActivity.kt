package com.example.bengkelappclient.ui.service

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bengkelappclient.databinding.ActivityServiceListBinding
import com.example.bengkelappclient.data.model.ServiceResult
import com.example.bengkelappclient.ui.adapter.ServiceAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ServiceListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityServiceListBinding
    private val viewModel: ServiceViewModel by viewModels()
    private lateinit var serviceAdapter: ServiceAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityServiceListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        observeServices()

        viewModel.fetchAllServices()
    }

    private fun setupRecyclerView() {
        serviceAdapter = ServiceAdapter()
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@ServiceListActivity)
            adapter = serviceAdapter
        }
    }

    private fun observeServices() {
        viewModel.allServices.observe(this) { result ->
            when (result) {
                is ServiceResult.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is ServiceResult.Success -> {
                    binding.progressBar.visibility = View.GONE
                    serviceAdapter.submitList(result.data)
                }
                is ServiceResult.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(
                        this,
                        "Gagal memuat layanan: ${result.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}
