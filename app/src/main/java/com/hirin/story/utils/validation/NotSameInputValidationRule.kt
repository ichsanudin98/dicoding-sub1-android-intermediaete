package com.hirin.story.utils.validation

class NotSameInputValidationRule(private val newPassword: String): ValidationRule<String> {
    override fun validate(data: String): Boolean {
        return data == newPassword
    }
}