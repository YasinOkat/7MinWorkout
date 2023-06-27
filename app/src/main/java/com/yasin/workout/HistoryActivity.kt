package com.yasin.workout

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.yasin.workout.databinding.ActivityHistoryBinding
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date

class HistoryActivity : AppCompatActivity() {

    private var binding: ActivityHistoryBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        val dateDao = (application as DateApp).db.dateDao()

        binding?.toolbarExercise?.title = "History"
        setSupportActionBar(binding?.toolbarExercise)

        if(supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        binding?.toolbarExercise?.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        lifecycleScope.launch { dateDao.fetchTable().collect{
            val list = ArrayList(it)
            setupListOfDataIntoRecyclerView(list, dateDao)
        } }
    }

    private fun setupListOfDataIntoRecyclerView(
        employeesList:ArrayList<DateEntity>,
        employeeDao: DateDao){
        if(employeesList.isNotEmpty()){
            val itemAdapter = ItemAdapter(employeesList)
            binding?.rvHistory?.layoutManager = LinearLayoutManager(this)
            binding?.rvHistory?.adapter = itemAdapter
            binding?.rvHistory?.visibility = View.VISIBLE
        }
        else{
            binding?.rvHistory?.visibility = View.GONE
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addRecord(dateDao: DateDao){
        lifecycleScope.launch{
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
            val current = LocalDateTime.now().format(formatter)
            dateDao.insert(DateEntity(date = current.toString()))
        }
    }
}