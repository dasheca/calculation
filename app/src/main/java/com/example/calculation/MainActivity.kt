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
        } catch (e: Exception) {
            display.text = "Error"
        }
    }

    fun onOperator(view: View) {
        try {
            firstNumber = display.text.toString().toDouble()
            operation = when ((view as Button).text.toString()) {
                "÷" -> "/"
                "×" -> "*"
                "−" -> "-"
                else -> "+"
            }
            isNewNumber = true
        } catch (e: Exception) {
            display.text = "Error"
        }
    }

    fun onEquals(view: View) {
        try {
            if (operation.isNotEmpty()) {
                val secondNumber = display.text.toString().toDouble()
                val result = when (operation) {
                    "+" -> firstNumber + secondNumber
                    "-" -> firstNumber - secondNumber
                    "*" -> firstNumber * secondNumber
                    "/" -> if (secondNumber != 0.0) {
                        firstNumber / secondNumber
                    } else {
                        display.text = "Error"
                        return
                    }
                    "^" -> Math.pow(firstNumber, secondNumber)
                    else -> 0.0
                }
                display.text = formatResult(result)
                operation = ""
                isNewNumber = true
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
    }

    fun onAbs(view: View) {
        try {
            val number = display.text.toString().toDouble()
            display.text = formatResult(Math.abs(number))
            isNewNumber = true
        } catch (e: Exception) {
            display.text = "Error"
        }
    }

    fun onPower(view: View) {
        try {
            firstNumber = display.text.toString().toDouble()
            operation = "^"
            isNewNumber = true
        } catch (e: Exception) {
            display.text = "Error"
        }
    }

    fun onLog(view: View) {
        try {
            val number = display.text.toString().toDouble()
            if (number > 0) {
                display.text = formatResult(Math.log10(number))
            } else {
                display.text = "Error"
            }
            isNewNumber = true
        } catch (e: Exception) {
            display.text = "Error"
        }
    }

    private fun formatResult(number: Double): String {
        return if (number == number.toLong().toDouble()) {
            number.toLong().toString()
        } else {
            String.format("%.8f", number).trimEnd('0').trimEnd('.')
        }
    }
}