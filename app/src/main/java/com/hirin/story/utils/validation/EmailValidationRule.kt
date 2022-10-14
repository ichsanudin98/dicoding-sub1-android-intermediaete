package com.hirin.story.utils.validation

import java.util.regex.Pattern

class EmailValidationRule : ValidationRule<String> {
    override fun validate(data: String): Boolean {
        return data.trim().matches(
            Pattern.compile(
                "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                        "\\@" +
                        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                        "(" +
                        "\\." +
                        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                        ")+"
            ).toRegex())
    }
}