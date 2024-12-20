package com.example.composecalculatorr

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class CalculatorViewModel : ViewModel() {

    var state by mutableStateOf(CalculatorState())
        private set

    fun onAction(action: CalculatorAction) {
        when (action) {
            is CalculatorAction.Number -> enterNumber(action.number)
            is CalculatorAction.Decimal -> enterDecimal()
            is CalculatorAction.Clear -> state = CalculatorState()
            is CalculatorAction.Operation -> enterOperation(action.operation)
            is CalculatorAction.Calculate -> performCalculation()
            is CalculatorAction.Delete -> performDeletion()
        }
    }

    private fun performDeletion() {
        if (state.numbers.isNotEmpty()) {
            val currentNumber = state.numbers.last()
            if (currentNumber.length > 1) {
                val updatedNumber = currentNumber.dropLast(1)
                state = state.copy(
                    numbers = state.numbers.dropLast(1) + updatedNumber
                )
            } else {
                state = state.copy(
                    numbers = state.numbers.dropLast(1)
                )
            }
        } else if (state.operators.isNotEmpty()) {
            state = state.copy(
                operators = state.operators.dropLast(1)
            )
        }
    }

    private fun performCalculation() {
        if (state.numbers.isEmpty() || state.operators.isEmpty()) return

        var result = state.numbers[0].toDoubleOrNull() ?: return

        for (i in 1 until state.numbers.size) {
            val operator = state.operators.getOrNull(i - 1) ?: continue
            val number = state.numbers[i].toDoubleOrNull() ?: continue

            result = when (operator) {
                CalculatorOperation.Add -> result + number
                CalculatorOperation.Subtract -> result - number
                CalculatorOperation.Multiply -> result * number
                CalculatorOperation.Divide -> if (number != 0.0) result / number else result
                CalculatorOperation.Percent -> result % number
            }
        }

        state = state.copy(
            numbers = listOf(result.toString()),
            operators = emptyList()
        )
    }

    private fun enterOperation(operation: CalculatorOperation) {
        if (state.numbers.isNotEmpty() && state.numbers.last().toDoubleOrNull() != null) {
            state = state.copy(
                operators = state.operators + operation
            )
        }
    }

    private fun enterDecimal() {
        if (state.numbers.isNotEmpty() && !state.numbers.last().contains(".")) {
            val updatedNumbers = state.numbers.dropLast(1) + (state.numbers.last() + ".")
            state = state.copy(numbers = updatedNumbers)
        } else if (state.numbers.isEmpty()) {
            state = state.copy(numbers = state.numbers + "0.")
        }
    }

    private fun enterNumber(number: Int) {
        if (state.numbers.isEmpty() || state.operators.size == state.numbers.size) {
            // Start a new number
            state = state.copy(
                numbers = state.numbers + number.toString()
            )
        } else {
            val updatedNumbers = state.numbers.dropLast(1) + (state.numbers.last() + number.toString())
            state = state.copy(numbers = updatedNumbers)
        }
    }
}
