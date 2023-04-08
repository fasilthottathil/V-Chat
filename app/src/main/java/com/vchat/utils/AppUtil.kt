package com.vchat.utils

import android.Manifest
import android.util.Patterns
import androidx.activity.ComponentActivity
import androidx.navigation.NavOptions
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.vchat.common.Constants
import com.vchat.common.annotations.EmailInput
import com.vchat.common.annotations.TextPasswordInput
import com.vchat.common.enums.InputType
import java.util.*


/**
 * Created by Fasil on 11/03/23.
 */
fun String?.validateInputText(inputType: InputType): Pair<Boolean, String?> {
    if (this.isNullOrEmpty()) {
        val message = when (inputType) {
            InputType.TEXT -> "Invalid input"
            InputType.EMAIL -> "Invalid email address"
            InputType.TEXT_PASSWORD, InputType.NUMBER_PASSWORD -> "Invalid password"
        }
        return Pair(false, message)
    }
    return when (inputType) {
        InputType.TEXT -> Pair(true, null)
        InputType.EMAIL -> {
            if (Patterns.EMAIL_ADDRESS.matcher(this).matches()) {
                Pair(true, null)
            } else {
                Pair(false, "Invalid email address")
            }
        }
        InputType.TEXT_PASSWORD, InputType.NUMBER_PASSWORD -> {
            if (this.length < 6) Pair(false, "Invalid password.Length should be greater than 5")
            else Pair(true, null)
        }
    }
}

fun popUpTo(route: String, inclusive: Boolean): NavOptions {
    return NavOptions.Builder().setPopUpTo(route, inclusive).build()
}

fun validateInputs(vararg inputs: @EmailInput @TextPasswordInput String): Pair<Boolean, String?> {
    return if (inputs.any { it.isEmpty() }) Pair(false, "Invalid input")
    else {
        val error = ""
        for (element in inputs) {
            element::class.annotations
            if (element::class == EmailInput::class) {
            }
        }
        if (error.isEmpty()) {
            return Pair(true, null)
        } else {
            Pair(false, error)
        }
    }
}

inline fun <reified T,reified O> T.mapObjectTo(): O? {
    val type = object : TypeToken<O>(){}.type
    return try {
        Gson().fromJson(Gson().toJson(this),type)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun Long.formatTimestamp(): String {
    val now = Date().time
    val diffInMillis = now - this

    val diffInSeconds = diffInMillis / 1000
    if (diffInSeconds < 60) {
        return "just now"
    }

    val diffInMinutes = diffInSeconds / 60
    if (diffInMinutes < 60) {
        return "$diffInMinutes min ago"
    }

    val diffInHours = diffInMinutes / 60
    if (diffInHours < 24) {
        return "$diffInHours ${if (diffInHours.toInt() == 1) "hr" else "hrs"} ago"
    }

    val diffInDays = diffInHours / 24
    if (diffInDays == 1L) {
        return "yesterday"
    }
    if (diffInDays < 7) {
        return "$diffInDays ${if (diffInDays.toInt() == 1) "day" else "days"} ago"
    }

    val diffInWeeks = diffInDays / 7
    if (diffInWeeks < 4) {
        return "$diffInWeeks ${if (diffInWeeks > 1) "weeks" else "week"} ago"
    }

    val diffInMonths = diffInDays / 30
    if (diffInMonths < 12) {
        return "$diffInMonths ${if (diffInMonths > 1) "months" else "month"} ago"
    }

    val diffInYears = diffInDays / 365
    return "$diffInYears ${if (diffInYears > 1) "years" else "year"} ago"
}


fun requestPermissions(context: ComponentActivity) {
    context.requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), Constants.READ_EXTERNAL_STORAGE_REQUEST)
}




