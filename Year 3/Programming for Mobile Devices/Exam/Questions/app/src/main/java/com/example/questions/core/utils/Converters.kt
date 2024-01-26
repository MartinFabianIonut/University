package com.example.questions.core.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun fromList(list: List<Int>): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun toList(json: String): List<Int> {
        val type = object : TypeToken<List<Int>>() {}.type
        return Gson().fromJson(json, type)
    }
}
