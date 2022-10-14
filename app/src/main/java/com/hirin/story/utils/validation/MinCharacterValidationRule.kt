package com.hirin.story.utils.validation

class MinCharacterValidationRule(private val minCharacter: Int) : ValidationRule<String> {
    override fun validate(data: String): Boolean {
        return data.length >= minCharacter
    }
}