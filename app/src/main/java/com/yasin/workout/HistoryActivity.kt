package com.yasin.workout

import android.app.AlertDialog
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
        dateList:ArrayList<DateEntity>,
        dateDao: DateDao){
        if(dateList.isNotEmpty()){
            val itemAdapter = ItemAdapter(dateList
            ) { deleteId ->
                deleteRecordAlertDialog(deleteId, dateDao)
            }

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

    private fun deleteRecordAlertDialog(id: Int, dateDao: DateDao){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete record")
        builder.setPositiveButton("Yes"){dialogInterface, _->
            lifecycleScope.launch {
                dateDao.delete(DateEntity(id))
                Toast.makeText(applicationContext, "Record deleted", Toast.LENGTH_LONG).show()
            }
            dialogInterface.dismiss()
        }
        builder.setNegativeButton("No"){dialogInterface, _->
            dialogInterface.dismiss()
        }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }
}