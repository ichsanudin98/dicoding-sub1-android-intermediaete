package com.hirin.story.utils.validation

interface ValidationRule<T> {
    fun validate(data: T): Boolean

    fun getExposedValidationRule(): ValidationRule<*> = this
}