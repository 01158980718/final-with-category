
package com.example.myapplicationdc.Activity

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplicationdc.Adapter.TopDoctorAdapter2
import com.example.myapplicationdc.ViewModel.MainViewModel
import com.example.myapplicationdc.databinding.ActivityTopDoctorBinding

class TopDoctorActivity : BaseActivity() {
    private lateinit var binding: ActivityTopDoctorBinding
    private val viewModel = MainViewModel()
    var patientId:Int?=null
    ////
    lateinit var item: String
    ////

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTopDoctorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        patientId = intent.getIntExtra("patientId", -3)

        binding.backBtn.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        item = intent.getStringExtra("object") ?: "" // Use an empty string if null

        if (item.isEmpty()) {
            Log.e("TopDoctorActivity", "Item is null or empty")
            // Optionally show a Toast or Snackbar to inform the user
            finish() // Close the activity if item is empty
            return
        }

        initTopDoctors()
    }

    private fun initTopDoctors() {
        binding.apply {
            progressBarTopDoctor.visibility = View.VISIBLE

            // Check if item is not null or empty
            if (item.isNullOrEmpty()) {
                Log.e("TopDoctorActivity", "Item is null or empty")
                progressBarTopDoctor.visibility = View.GONE
                return
            }

            viewModel.doctor.observe(this@TopDoctorActivity, Observer { doctors ->
                if (doctors != null && doctors.isNotEmpty()) {
                    viewTopDoctorList.layoutManager = LinearLayoutManager(this@TopDoctorActivity, LinearLayoutManager.VERTICAL, false)
                    viewTopDoctorList.adapter = TopDoctorAdapter2(doctors, patientId)
                    progressBarTopDoctor.visibility = View.GONE
                } else {
                    Log.d("TopDoctorActivity", "No doctors data available.")
                    progressBarTopDoctor.visibility = View.GONE
                }
            })

            // Load doctors with error handling
            viewModel.loadDoctors(item)
        }
    }
}
