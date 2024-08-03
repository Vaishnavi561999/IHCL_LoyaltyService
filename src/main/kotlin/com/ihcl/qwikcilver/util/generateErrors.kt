package com.ihcl.qwikcilver.util

import io.konform.validation.ValidationResult

fun <T> generateErrors(validationResult: ValidationResult<T>): List<String> {
    val errors = mutableListOf<String>()
    validationResult.errors.forEach { error ->
        errors.add(
            "${error.dataPath.substring(1)} ${error.message}"
        )
    }
    return errors
}
