package com.raulolmedo.mycalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.*

class MainActivity : AppCompatActivity() {
    companion object {
        enum class Operation {
            ADD, SUBTRACT, MULTIPLY, DIVIDE, POWER
        }
    }

    var isFloat: Boolean = false
    var isShowingResult: Boolean = false

    var leftOperand: Float = 0F
    var rightOperand: Float = 0F

    var currentOperation: Operation? = null

    var memoryRegister: Float = 0F

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState != null) {
            isFloat = savedInstanceState.getBoolean("is_float")
            isShowingResult = savedInstanceState.getBoolean("is_showing_result")
            leftOperand = savedInstanceState.getFloat("left_operand")
            rightOperand = savedInstanceState.getFloat("right_operand")
            currentOperation = savedInstanceState.getString("current_operation")?.let { Operation.valueOf(it) }
            memoryRegister = savedInstanceState.getFloat("memory_register")
            text_view_result.text = savedInstanceState.getString("text_view_result")
        }

        /* Numbers */
        button_0.setOnClickListener{ onButtonPressed("0") }

        button_1.setOnClickListener{ onButtonPressed("1") }

        button_2.setOnClickListener{ onButtonPressed("2") }

        button_3.setOnClickListener{ onButtonPressed("3") }

        button_4.setOnClickListener{ onButtonPressed("4") }

        button_5.setOnClickListener{ onButtonPressed("5") }

        button_6.setOnClickListener{ onButtonPressed("6") }

        button_7.setOnClickListener{ onButtonPressed("7") }

        button_8.setOnClickListener{ onButtonPressed("8") }

        button_9.setOnClickListener{ onButtonPressed("9") }

        button_decimal.setOnClickListener{
            if (!isFloat) {
                text_view_result.text = text_view_result.text.toString() + "."
                isFloat = true
            }
        }

        /* Operators */
        button_add.setOnClickListener{ setOperator(Operation.ADD) }

        button_subtract.setOnClickListener{ setOperator(Operation.SUBTRACT) }

        button_multiply.setOnClickListener{ setOperator(Operation.MULTIPLY) }

        button_divide.setOnClickListener{ setOperator(Operation.DIVIDE) }

        button_equal.setOnClickListener{
            text_view_result.text = operate(true).toString()
        }


        /* Modifiers */
        button_delete.setOnClickListener{
            text_view_result.text = "0"
            isShowingResult = true
        }

        button_clear.setOnClickListener{
            resetCalculator()
            text_view_result.text = "0"
            isShowingResult = true
        }

        button_memory_add.setOnClickListener{
            text_view_result.text = memoryRegister.toString()
        }

        button_memory?.setOnClickListener{
            memoryRegister = text_view_result.text.toString().toFloat()
        }

        button_memory_clear?.setOnClickListener{
            memoryRegister = 0F
        }

        button_memory_recall?.setOnClickListener{
            text_view_result.text = memoryRegister.toString()
        }

        button_memory_subtract?.setOnClickListener{
            text_view_result.text = (memoryRegister * -1).toString()
        }


        /* Functions */
        button_power?.setOnClickListener{ setOperator(Operation.POWER) }

        button_sqrt.setOnClickListener{
            val result = sqrt(text_view_result.text.toString().toFloat())
            text_view_result.text = result.toString()
        }

        button_percentage.setOnClickListener{
            val result = text_view_result.text.toString().toFloat() / 100
            text_view_result.text = result.toString()
        }

        button_sign.setOnClickListener{
            val result = -1 * text_view_result.text.toString().toFloat()
            text_view_result.text = result.toString()
        }

        button_square?.setOnClickListener{
            val result = text_view_result.text.toString().toFloat().pow(2)
            text_view_result.text = result.toString()
        }

        button_sin?.setOnClickListener{
            val result = sin(text_view_result.text.toString().toFloat())
            text_view_result.text = result.toString()
        }

        button_cos?.setOnClickListener{
            val result = cos(text_view_result.text.toString().toFloat())
            text_view_result.text = result.toString()
        }

        button_tan?.setOnClickListener{
            val result = tan(text_view_result.text.toString().toFloat())
            text_view_result.text = result.toString()
        }

        button_divide_by_x?.setOnClickListener{
            val result = text_view_result.text.toString().toFloat().pow(-1)
            text_view_result.text = result.toString()
        }

        button_pi?.setOnClickListener{
            text_view_result.text = PI.toString()
        }


        /* Others */
        button_off?.setOnClickListener{
            resetCalculator()
            text_view_result.text = ""
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putBoolean("is_float", isFloat)
        outState.putBoolean("is_Showing_result", isShowingResult)
        outState.putFloat("left_operand", leftOperand)
        outState.putFloat("right_operand", rightOperand)
        outState.putString("current_operation", currentOperation?.name)
        outState.putFloat("memory_register", memoryRegister)
        outState.putString("text_view_result", text_view_result.text.toString())
    }

    private fun setOperator(selectedOperation: Operation) {
        if (currentOperation != null) {
            // An operation is being made
            rightOperand = text_view_result.text.toString().toFloat()
            val result = operate(false)
            text_view_result.text = result.toString()
            leftOperand = result
            currentOperation = selectedOperation

        } else {
            // No operation was being made, normal workflow
            currentOperation = selectedOperation
            leftOperand = text_view_result.text.toString().toFloat()
            text_view_result.text = null
        }

        isFloat = false

    }

    private fun operate(isFinalOperation: Boolean): Float {
        var result: Float = 0F
        rightOperand = text_view_result.text.toString().toFloat()

        when (currentOperation) {
            Operation.ADD -> result = leftOperand + rightOperand
            Operation.SUBTRACT -> result = leftOperand - rightOperand
            Operation.MULTIPLY -> result = leftOperand * rightOperand
            Operation.DIVIDE -> result = leftOperand / rightOperand
            Operation.POWER -> result = leftOperand.pow(rightOperand)
            else -> print("no operation was selected")
        }

        if (isFinalOperation) {
            resetCalculator()
        }

        isShowingResult = true
        return result
    }

    private fun resetCalculator() {
        leftOperand = 0F
        rightOperand = 0F
        currentOperation = null
        isFloat = false
    }

    private fun onButtonPressed(element: String) {
        if (isShowingResult) {
            text_view_result.text = element
            isShowingResult = false
        } else {
            text_view_result.text = text_view_result.text.toString() + element
        }
    }
}