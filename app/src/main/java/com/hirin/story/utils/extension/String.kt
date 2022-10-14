package com.hirin.story.utils.extension

import java.math.BigInteger
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit

fun String?.orDash(): String = this.takeIf { !it.isNullOrBlank() } ?: "-"

fun String.md5(): String {
    val md = MessageDigest.getInstance("MD5")
    return BigInteger(1, md.digest(this.toByteArray())).toString(16).padStart(32, '0')
}

fun String.convertDate(
    formatDate: String, date: String,
    latestTitle: String, minuteTitle: String, hourTitle: String,
    dayTitle: String, agoTitle: String
): String {
    return try {
        val time2 = System.currentTimeMillis()
        var time = SimpleDateFormat(formatDate).parse(date)?.time!!
        val MINUTE = 1
        val HOUR_MINUTES = 60 * MINUTE
        val DAY_HOURS = 24 * HOUR_MINUTES
        val diffByString: String
        val diffByHours: Long
        val diffByMinutes: Long
        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000
        }
        if (time > time2 || time <= 0) {
            return date
        }
        val diff = time2 - time
        diffByHours = TimeUnit.HOURS.convert(diff, TimeUnit.MILLISECONDS)
        diffByMinutes = TimeUnit.MINUTES.convert(diff, TimeUnit.MILLISECONDS)
        diffByString = when {
            diffByMinutes < MINUTE -> {
                latestTitle
            }
            diffByMinutes < 2 * MINUTE -> {
                "1 $minuteTitle $agoTitle"
            }
            diffByMinutes < 50 * MINUTE -> {
                (diffByMinutes / MINUTE).toString() + " $minuteTitle $agoTitle"
            }
            diffByMinutes < 90 * MINUTE -> {
                "1 $hourTitle $agoTitle"
            }
            diffByMinutes < 24 * HOUR_MINUTES -> {
                (diffByMinutes / HOUR_MINUTES).toString() + " $hourTitle $agoTitle"
            }
            diffByMinutes < 48 * HOUR_MINUTES -> {
                "1 $dayTitle $agoTitle"
            }
            (diffByMinutes / DAY_HOURS) <= 28 -> {
                (diffByMinutes / DAY_HOURS).toString() + " $dayTitle $agoTitle"
            }
            else -> {
                date
            }
        }

        diffByString
    } catch (ex: Exception) {
        date
    }
}

fun String.getFirstLetterWords(): String {
    var result = ""

    // Traverse the string.

    // Traverse the string.
    var v = true
    for (i in 0 until this.length) {
        // If it is space, set v as true.
        if (this[i] === ' ') {
            v = true
        } else if (this[i] !== ' ' && v) {
            result += this[i]
            v = false
        }

        if (result.length >= 3)
            break
    }

    return result
}