package com.example.logbook

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.logbook.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var input = ""
    private var firstNum = 0.0
    private var currentOperator = ""
    private var isNewOperation = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        clickListeners()
        showResult()

    }
    // to listen for button clicks on calculator
    private fun clickListeners() {
        binding.btnZero.setOnClickListener { onNumberClick("0") }
        binding.btnOne.setOnClickListener { onNumberClick("1") }
        binding.btnTwo.setOnClickListener { onNumberClick("2") }
        binding.btnThree.setOnClickListener { onNumberClick("3")}
        binding.btnFour.setOnClickListener { onNumberClick("4") }
        binding.btnFive.setOnClickListener { onNumberClick("5") }
        binding.btnSix.setOnClickListener { onNumberClick("6") }
        binding.btnSeven.setOnClickListener { onNumberClick("7")}
        binding.btnEight.setOnClickListener { onNumberClick("8")}
        binding.btnNine.setOnClickListener { onNumberClick("9") }
        binding.btnPlus.setOnClickListener { onOperatorClick("+") }
        binding.btnMinus.setOnClickListener { onOperatorClick("-") }
        binding.btnMultiply.setOnClickListener { onOperatorClick("×") }
        binding.btnDivide.setOnClickListener { onOperatorClick("/") }
        binding.btnEqual.setOnClickListener { onEqualClick() }
        binding.btnClearAll.setOnClickListener { onClearClick() }
        binding.btnDecimal.setOnClickListener { onDecimalClick() }
        binding.btnClearOne.setOnClickListener { onBackspaceClick() }
        binding.btnSignSwitch.setOnClickListener { signSwitch() }
        binding.btnPercentage.setOnClickListener { PercentageClick() }
    }
    //to display current input or '0' at rest
    private fun showResult() {


        val display = when {
            currentOperator.isEmpty() -> {
                if (input.isEmpty()) "0" else input
            }
            input.isEmpty() -> {

                "${formatResult(firstNum)} $currentOperator"
            }
            else -> {

                "${formatResult(firstNum)} $currentOperator $input"
            }
        }
        binding.txtView.text = display
    }
    //append numbers that are clicked
    private fun onNumberClick(number: String) {
        if (isNewOperation) {
            input = number
            isNewOperation = false
        } else {
            input += number
        }
        showResult()
    }

    //to add a decimal point if it doesn't exist yet
    private fun onDecimalClick() {
        if (!input.contains(".")) {
            input += if (input.isEmpty()) "0." else "."
            showResult()
        }
    }

    //store first number and set operator
    private fun onOperatorClick(operator: String) {

        if (input.isNotEmpty()) {
            if (currentOperator.isNotEmpty()) {
                onEqualClick()

            }
            else{

            }
            firstNum = input.toDouble()


            currentOperator = operator

            binding.txtView.text = "${formatResult(firstNum)} $operator"

            isNewOperation = true
        }
    }

    //perform calculation and display results
    private fun onEqualClick() {
        if (currentOperator.isEmpty() || input.isEmpty()) return

        val secondNum = input.toDouble()

        val result = when (currentOperator) {
            "+" -> firstNum + secondNum
            "-" -> firstNum - secondNum
            "×" -> firstNum * secondNum
            "/" -> {
                if (secondNum == 0.0) {
                    showError("Cannot be divided by zero")
                    return
                }
                firstNum / secondNum
            }
            else -> return
        }

        input = formatResult(result)
        currentOperator = ""
        isNewOperation = true
        showResult()
    }

    //resets all calculator value
    private fun onClearClick() {
        input = ""
        firstNum = 0.0
        currentOperator = ""
        isNewOperation = true
        showResult()
    }

    // error validation message
    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        input = ""
        showResult()
    }

    //to format results to 6 decimal place and cut ending zeros and decimals
    private fun formatResult(result: Double): String {

        return if (result % 1.0 == 0.0) {
            result.toInt().toString()
        } else {
            String.format("%.6f", result).trimEnd('0').trimEnd('.')
        }
    }
    // back space function to drop last 1 character
    private fun onBackspaceClick() {
        if (input.isNotEmpty()) {
            input = input.dropLast(1)
            showResult()
        }
    }

    //to switch signs for existing number with validation
    private fun signSwitch() {
        if (input.isNotEmpty() && input != "0") {
            input = if (input.startsWith("-"))
            {
                input.removePrefix("-")
            } else {
                "-$input"
            }
            showResult()
        }
    }

    //for converting numbers into percentages
    private fun PercentageClick() {
        if (input.isNotEmpty()) {
            val value = input.toDouble()
            input = formatResult(value / 100)
            showResult()
        }
    }


}