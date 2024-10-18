package com.example.myapplicationdc.Activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplicationdc.Adapter.CategoryAdapter
import com.example.myapplicationdc.Adapters.TopDoctorAdapter
import com.example.myapplicationdc.ViewModel.MainViewModel
import com.example.myapplicationdc.databinding.ActivityMainBinding
class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel = MainViewModel()
    private var patientId: Int?=0
    private var patientName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve the patientId and patientName passed from SignInActivity
        patientId = intent.getIntExtra("patientId",5)
        patientName = intent.getStringExtra("PATIENT_NAME")
        binding.textView2.text = "Hi $patientName"

        // Check if patientId is available
        patientId?.let {
            // Use patientId if needed (e.g., fetch patient-specific data)
            println("Logged in patient ID: $it")
        }

        initCategory()
        initTopDoctor()

        // Navigate to TopDoctorActivity
        binding.doctorlistText.setOnClickListener {
            val intent = Intent(this@MainActivity, TopDoctorActivity::class.java)
            intent.putExtra("patientId", patientId)
            startActivity(intent)
        }

        // Navigate to HistoryActivity
        binding.history.setOnClickListener {
            val intent = Intent(this@MainActivity, HistoryActivity::class.java)
            intent.putExtra("patientId", patientId)
            startActivity(intent)
        }

        // Navigate to Home (restarting MainActivity)
        binding.home.setOnClickListener {
            val intent = Intent(this@MainActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initTopDoctor() {
        binding.progressBarTopDoctors.visibility = View.VISIBLE

        viewModel.doctor.observe(this@MainActivity, Observer { doctors ->
            // Set up RecyclerView layout and adapter
            binding.recyclerViewTopDoctors.layoutManager = LinearLayoutManager(
                this@MainActivity,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            binding.recyclerViewTopDoctors.adapter = TopDoctorAdapter(doctors,
                (patientId)
            )

            // Hide progress bar after data is loaded
            binding.progressBarTopDoctors.visibility = View.GONE
        })

        ////
        viewModel.loadDoctors(null)
    }

    private fun initCategory() {
        binding.progressBarCategory.visibility = View.VISIBLE

        viewModel.category.observe(this@MainActivity, Observer { categories ->
            // Set up RecyclerView layout and adapter
            binding.viewCategory.layoutManager = LinearLayoutManager(
                this@MainActivity,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            binding.viewCategory.adapter = CategoryAdapter(categories)

            // Hide progress bar after data is loaded
            binding.progressBarCategory.visibility = View.GONE
        })

        viewModel.loadCategory()
    }
}
