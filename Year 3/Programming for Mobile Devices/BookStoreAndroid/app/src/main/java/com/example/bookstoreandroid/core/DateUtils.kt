package com.example.bookstoreandroid.core

import android.util.Log
import java.text.SimpleDateFormat
import java.util.*
import com.example.bookstoreandroid.core.TAG
class DateUtils {

    companion object {
        fun convertToDDMMYYYY(inputDate: String): String? {
            return try {
                val dateFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss 'GMT'Z yyyy", Locale.getDefault())
                val date = dateFormat.parse(inputDate)

                val formattedDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                formattedDateFormat.format(date)
            } catch (e: Exception) {
                Log.d(TAG, "Error converting date: ${e.toString()}")
                return null
            }
        }

        fun parseDDMMYYYY(dateString: String): Date? {
            try {
                val (day, month, year) = dateString.split('/').map { it.toInt() }

                if (day < 1 || month < 1 || year < 1) {
                    Log.e(TAG, "Invalid date")
                    return null
                }

                val adjustedMonth = month - 1
                val calendar = Calendar.getInstance()
                calendar.timeZone = TimeZone.getTimeZone("UTC")
                calendar.set(year, adjustedMonth, day, 0, 0, 0)
                calendar.set(Calendar.MILLISECOND, 0)

                val parsedDate = calendar.time

                if (
                    calendar.get(Calendar.DAY_OF_MONTH) != day ||
                    calendar.get(Calendar.MONTH) != adjustedMonth ||
                    calendar.get(Calendar.YEAR) != year
                ) {
                    Log.e(TAG, "Invalid date")
                    return null
                }

                if (parsedDate.time < 0) {
                    Log.e(TAG, "Invalid date")
                    return null
                }

                return Date(parsedDate.time)
            } catch (e: Exception) {
                // Handle parsing errors
                Log.e(TAG, "Error parsing date: ${e.toString()}")
                return null
            }
        }
    }
}
