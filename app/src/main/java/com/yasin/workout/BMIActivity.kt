package com.yasin.workout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.yasin.workout.databinding.ActivityBmiactivityBinding
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.pow

class BMIActivity : AppCompatActivity() {

    private var binding: ActivityBmiactivityBinding? = null
    private var flag: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBmiactivityBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.toolbarExercise?.title = "BMI Calculator"
        setSupportActionBar(binding?.toolbarExercise)

        binding?.rbMetric?.isChecked = true
        binding?.rbBurger?.isChecked = false

        binding?.llBurger?.visibility = View.INVISIBLE
        binding?.tiHeight?.visibility = View.VISIBLE
        binding?.tiWeight?.visibility = View.VISIBLE


        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        binding?.toolbarExercise?.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding?.rgUnits?.setOnCheckedChangeListener{ group, checkedId ->
            if (checkedId == binding?.rbMetric?.id) {
                binding?.llBurger?.visibility = View.INVISIBLE
                binding?.tiWeight?.visibility = View.VISIBLE
                binding?.tiHeight?.visibility = View.VISIBLE
            } else if (checkedId == binding?.rbBurger?.id) {
                binding?.llBurger?.visibility = View.VISIBLE
                binding?.tiHeight?.visibility = View.INVISIBLE
                binding?.tiWeight?.visibility = View.INVISIBLE
            }
        }

        binding?.btnCalculate?.setOnClickListener {
            val height = binding?.etHeight?.text.toString().toDoubleOrNull()
            val weight = binding?.etWeight?.text.toString().toDoubleOrNull()
            val weightPounds = binding?.etUsWeight?.text.toString().toDoubleOrNull()
            val feet = binding?.etFeet?.text.toString().toDoubleOrNull()
            val inch = binding?.etInch?.text.toString().toDoubleOrNull()

            if (binding?.rbMetric?.isChecked == true){
                if (height != null && weight != null) {
                    val bmi = calculateBMI(height, weight)
                    binding?.tvYourbmi?.text = "YOUR BMI"
                    binding?.tvBmi?.text = bmi.toString()
                    binding?.tvResultbmi?.text = resultBMI(bmi)
                    binding?.tvMessagebmi?.text = messageBMI(bmi)
                }
            }
            else if (binding?.rbBurger?.isChecked == false){
                if (weightPounds != null && feet != null && inch != null){
                    val bmi = calculateBMIinUS(inch, feet, weightPounds)
                    binding?.tvYourbmi?.text = "YOUR BMI"
                    binding?.tvBmi?.text = bmi.toString()
                    binding?.tvResultbmi?.text = resultBMI(bmi)
                    binding?.tvMessagebmi?.text = messageBMI(bmi)
                }
            }
        }
    }

    private fun calculateBMI(height: Double, weight: Double): Double {
        val heightInMeters = height / 100.0
        return round(weight / (heightInMeters * heightInMeters))
    }

    private fun calculateBMIinUS(inch: Double, feet: Double, weightPounds: Double): Double {
        val heightInInches = feet * 12 + inch
        return round(weightPounds / (heightInInches * heightInInches) * 703)
    }

    private fun resultBMI(bmi: Double): String {
        return when {
            bmi < 18.5 -> "Underweight"
            bmi < 25 -> "Normal"
            bmi < 30 -> "Overweight"
            bmi < 35 -> "Almost Obesity"
            else -> "Obesity"
        }
    }

    private fun messageBMI(bmi: Double): String {
        return when {
            bmi < 18.5 -> "You really need to gain weight"
            bmi < 25 -> "You are OK"
            bmi < 30 -> "You need to lose weight"
            bmi < 35 -> "Things are getting dangerous"
            else -> "You have obesity, you will have health problems if you don't have currently"
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    private fun round(number: Double): Double {
        return BigDecimal(number)
            .setScale(2, RoundingMode.HALF_UP)
            .toDouble()
    }
}
