package com.example.composecalculatorr

data class CalculatorState(
    val numbers: List<String> = emptyList(),
    val operators: List<CalculatorOperation> = emptyList()
)
