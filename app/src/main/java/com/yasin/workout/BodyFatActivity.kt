package com.yasin.workout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import com.yasin.workout.databinding.ActivityBodyFatBinding
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.log10

class BodyFatActivity : AppCompatActivity() {

    private var binding : ActivityBodyFatBinding? = null
    private var height: Double? = null
    private var neck: Double? = null
    private var waist: Double? = null
    private var maleCheckBox: CheckBox? = null
    private var femaleCheckBox: CheckBox? = null
    private var hip: Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBodyFatBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.toolbarExercise?.title = "Body Fat Calculator"
        setSupportActionBar(binding?.toolbarExercise)

        maleCheckBox = binding?.cbMale
        femaleCheckBox = binding?.cbFemale

        maleCheckBox?.isChecked = true
        femaleCheckBox?.isChecked = false

        if(supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        binding?.toolbarExercise?.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding?.btnCalculate?.setOnClickListener{
            height = binding?.etHeight?.text.toString().toDoubleOrNull()
            neck = binding?.etNeck?.text.toString().toDoubleOrNull()
            waist = binding?.etWaist?.text.toString().toDoubleOrNull()
            hip = binding?.etHip?.text.toString().toDoubleOrNull()

            calculateBodyFat()

        }

        binding?.cbMale?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding?.cbFemale?.isChecked = false
                binding?.cbMale?.isChecked = true
                binding?.etHip?.visibility = View.GONE
                binding?.tiHip?.visibility = View.GONE
            }
        }

        binding?.cbFemale?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding?.cbFemale?.isChecked = true
                binding?.cbMale?.isChecked = false
                binding?.etHip?.visibility = View.VISIBLE
                binding?.tiHip?.visibility = View.VISIBLE
            }
        }
    }

    private fun calculateBodyFat(){
        if(height != null && neck != null && waist != null) {
            if (maleCheckBox?.isChecked == true) {
                val bodyFatPercentage = round(
                    495 / (1.0324 - 0.19077 * log10(waist!! - neck!!) + 0.15456 * log10(
                        height!!
                    )) - 450
                )

                binding?.tvBodyFat?.text = bodyFatPercentage.toString() + "%"
            }
        }
        if(height != null && neck != null && waist != null && hip != null) {
            if (femaleCheckBox?.isChecked == true) {
                val bodyFatPercentage =
                    round(495 / (1.29579 - 0.35004 * log10(waist!! + hip!! - neck!!) + 0.22100 * log10(
                        height!!
                    )) - 450)

                binding?.tvBodyFat?.text = bodyFatPercentage.toString() + "%"
            }
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