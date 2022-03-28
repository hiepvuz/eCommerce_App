package com.example.sello.validate

import android.text.TextUtils
import android.util.Patterns
import java.util.regex.Pattern

class Validate {
    fun checkPass(string: String): Boolean {
        val regexPass = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$"
        return Pattern.compile(regexPass).matcher(string).matches()
    }

    fun isValidEmail(email: String): Boolean {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun validCellPhone(number: String): Boolean {
        val regexPhone = "(84|0[3|5|7|8|9])+([0-9]{8})\\b"
        return Pattern.compile(regexPhone).matcher(number).matches()
    }
}