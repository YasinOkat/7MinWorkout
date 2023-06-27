package com.yasin.workout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import com.yasin.workout.databinding.ActivityCalorieNeedBinding
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.log10

class CalorieNeedActivity : AppCompatActivity() {

    private var binding : ActivityCalorieNeedBinding? = null
    private var weight: EditText? = null
    private var bodyFat: EditText? = null
    private var calculate: Button? = null
    private var bmrTv: TextView? = null
    private var calorieNeedTv: TextView? = null
    private var activityLevelSpinner: Spinner? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalorieNeedBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.toolbarExercise?.title = "Daily Calorie Need Calculator"
        setSupportActionBar(binding?.toolbarExercise)

        weight = binding?.etWeight
        bodyFat = binding?.etBodyFat
        activityLevelSpinner = binding?.spinnerActivityLevel
        calculate = binding?.btnCalculate
        bmrTv = binding?.tvBMR
        calorieNeedTv = binding?.tvCalorieNeed

        if(supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        setupActivityLevelSpinner()

        binding?.toolbarExercise?.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding?.btnCalculate?.setOnClickListener{
            calculateBMRAndCalorieNeeds()
        }
    }

    private fun setupActivityLevelSpinner() {
        val activityLevels = arrayOf(
            "Sedentary (no exercise)",
            "Light (1-3 day of exercise)",
            "Moderate (3-5 day of exercise)",
            "Active (5-7 day of exercise)",
            "Very Active (5-7 day of exercise)"
        )

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            activityLevels
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        activityLevelSpinner?.adapter = adapter
    }

    private fun calculateBMRAndCalorieNeeds() {
        val weightText = weight?.text.toString().trim()
        val bodyFatText = bodyFat?.text.toString().trim()

        if (weightText.isEmpty() || bodyFatText.isEmpty()) {
            bmrTv?.text = getString(R.string.enter_values)
            calorieNeedTv?.text = ""
            return
        }

        val weight = weightText.toDoubleOrNull() ?: 0.0
        val bodyFatRatio = bodyFatText.toDoubleOrNull() ?: 0.0
        val activityLevel = activityLevelSpinner?.selectedItem.toString()

        val bmr = calculateBMR(weight, bodyFatRatio)
        val dailyCalorieNeeds = calculateDailyCalorieNeeds(bmr, activityLevel)

        bmrTv?.text = getString(R.string.bmr_label).format(bmr)
        calorieNeedTv?.text = getString(R.string.calorie_needs_label).format(dailyCalorieNeeds)
    }

    private fun calculateBMR(weight: Double, bodyFatRatio: Double): Double {
        val leanBodyMass = weight * (100 - bodyFatRatio) / 100
        return 370 + (21.6 * leanBodyMass)
    }

    private fun calculateDailyCalorieNeeds(bmr: Double, activityLevel: String): Double {
        return when (activityLevel) {
            "Sedentary (no exercise)"-> bmr * 1.2
            "Light (1-3 day of exercise)"-> bmr * 1.375
            "Moderate (3-5 day of exercise)" -> bmr * 1.55
            "Active (5-7 day of exercise)" -> bmr * 1.725
            "Very Active (5-7 day of exercise)" -> bmr * 1.9
            else -> bmr
        }
    }
}