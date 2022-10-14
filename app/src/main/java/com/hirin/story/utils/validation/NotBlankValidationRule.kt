package com.hirin.story.utils.validation

class NotBlankValidationRule: ValidationRule<String> {
    override fun validate(data: String): Boolean {
        return data.isNotBlank()
    }
}