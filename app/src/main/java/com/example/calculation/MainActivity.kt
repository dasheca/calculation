package com.example.calculation

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var display: TextView
    private var firstNumber = 0.0
    private var operation = ""
    private var isNewNumber = true
    private var isWaitingForOperation = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        display = findViewById(R.id.display)
    }

    fun openDocumentation(view: View) {
        val intent = Intent(this, DocumentationActivity::class.java)
        startActivity(intent)
    }

    fun onDigit(view: View) {
        try {
            val digit = (view as Button).text.toString()
            if (isNewNumber) {
                display.text = digit
                isNewNumber = false
            } else {
                display.text = display.text.toString() + digit
            }
            isWaitingForOperation = false
        } catch (e: Exception) {
            display.text = "Error"
        }
    }

    fun onOperator(view: View) {
        try {
            val operatorText = (view as Button).text.toString()
            val currentText = display.text.toString()

            // Если это первый минус (для отрицательного числа)
            if (operatorText == "−" && (isNewNumber || currentText == "0")) {
                display.text = "-"
                isNewNumber = false
                return
            }

            // Если уже есть минус в начале и снова нажат минус
            if (operatorText == "−" && currentText == "-") {
                return
            }

            if (currentText.isEmpty() || currentText == "Error" || currentText == "-") {
                display.text = "Error"
                return
            }

            // Проверка на корректный ввод дробного числа
            if (currentText.contains(".") && !isValidDecimalNumber(currentText)) {
                display.text = "Error: Введите дробную часть"
                return
            }
            
            firstNumber = currentText.toDouble()
            operation = when (operatorText) {
                "÷" -> "/"
                "×" -> "*"
                "−" -> "-"
                else -> "+"
            }
            isNewNumber = true
            isWaitingForOperation = true
        } catch (e: Exception) {
            display.text = "Error"
        }
    }

    fun onEquals(view: View) {
        try {
            if (operation.isNotEmpty()) {
                val currentText = display.text.toString()
                if (currentText.isEmpty() || currentText == "Error" || currentText == "-") {
                    display.text = "Error"
                    return
                }

                // Проверка на корректный ввод дробного числа
                if (currentText.contains(".") && !isValidDecimalNumber(currentText)) {
                    display.text = "Error"
                    return
                }

                val secondNumber = currentText.toDouble()
                val result = when (operation) {
                    "+" -> firstNumber + secondNumber
                    "-" -> firstNumber - secondNumber
                    "*" -> firstNumber * secondNumber
                    "/" -> {
                        if (secondNumber == 0.0) {
                            display.text = "Error"
                            return
                        }
                        firstNumber / secondNumber
                    }
                    "^" -> Math.pow(firstNumber, secondNumber)
                    else -> 0.0
                }
                display.text = formatResult(result)
                operation = ""
                isNewNumber = true
                isWaitingForOperation = false
            }
        } catch (e: Exception) {
            display.text = "Error"
        }
    }

    fun onClear(view: View) {
        display.text = "0"
        firstNumber = 0.0
        operation = ""
        isNewNumber = true
        isWaitingForOperation = false
    }

    fun onDecimalPoint(view: View) {
        try {
            val currentText = display.text.toString()
            if (isNewNumber) {
                display.text = "0."
                isNewNumber = false
            } else if (!currentText.contains(".")) {
                display.text = "$currentText."
            }
        } catch (e: Exception) {
            display.text = "Error"
        }
    }

    fun onAbs(view: View) {
        try {
            val currentText = display.text.toString()
            if (currentText.contains(".") && !isValidDecimalNumber(currentText)) {
                display.text = "Error: Введите дробную часть"
                return
            }
            val number = currentText.toDouble()
            display.text = formatResult(Math.abs(number))
            isNewNumber = true
        } catch (e: Exception) {
            display.text = "Error"
        }
    }

    fun onPower(view: View) {
        try {
            val currentText = display.text.toString()
            if (currentText.contains(".") && !isValidDecimalNumber(currentText)) {
                display.text = "Error: Введите дробную часть"
                return
            }
            firstNumber = currentText.toDouble()
            operation = "^"
            isNewNumber = true
        } catch (e: Exception) {
            display.text = "Error"
        }
    }

    fun onLog(view: View) {
        try {
            val currentText = display.text.toString()
            if (currentText.contains(".") && !isValidDecimalNumber(currentText)) {
                display.text = "Error: Введите дробную часть"
                return
            }
            val number = currentText.toDouble()
            if (number <= 0) {
                display.text = "Error: Число должно быть больше нуля"
                return
            }
            display.text = formatResult(Math.log(number))
            isNewNumber = true
        } catch (e: Exception) {
            display.text = "Error"
        }
    }

    private fun isValidDecimalNumber(number: String): Boolean {
        return !number.endsWith(".") && number.substringAfter(".").isNotEmpty()
    }

    private fun formatResult(number: Double): String {
        val result = if (number == number.toLong().toDouble()) {
            number.toLong().toString()
        } else {
            String.format("%.8f", number).trimEnd('0').trimEnd('.')
        }
        
        return if (number < 0) "-${result.removePrefix("-")}" else result
    }
}