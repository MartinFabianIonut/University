package com.example.messages.core

import android.util.Log
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class DateUtils {

    companion object {
        fun convertToDDMMYYYY(inputDate: String): String? {
            return try {
                val dateFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss 'GMT'Z yyyy", Locale.getDefault())
                val date = dateFormat.parse(inputDate)

                val formattedDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                formattedDateFormat.format(date)
            } catch (e: Exception) {
                Log.e(TAG, "Error converting date: $e")
                return null
            }
        }

        fun parseDDMMYYYY(dateString: String): Date? {
            Log.d(TAG, "Parsing date: $dateString")
            val formats = listOf(
                SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()),
                SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()),
                SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            )

            for (format in formats) {
                try {
                    format.isLenient = false
                    val parsedDate = format.parse(dateString)
                    Log.d(TAG, "Parsed date: $parsedDate")
                    if (parsedDate != null) {
                        val yearFormat = SimpleDateFormat("yyyy", Locale.getDefault())
                        val yearString = yearFormat.format(parsedDate)
                        val year = yearString.toInt()
                        val currentYear = Calendar.getInstance(TimeZone.getDefault()).get(Calendar.YEAR)
                        if (year in 1000..currentYear) {
                            Log.i(TAG, "Date parsed successfully")
                            return parsedDate
                        }
                    }
                }
                catch (e: Exception) {
                    Log.e(TAG, "Error parsing date: $e")
                }
            }
            return null
        }
    }
}
