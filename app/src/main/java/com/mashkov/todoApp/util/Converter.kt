package com.mashkov.todoApp.util

import androidx.room.TypeConverter
import com.mashkov.todoApp.model.Priority
import java.util.*

object Converter {
    @JvmStatic
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return if (value == null) null else Date(value)
    }

    @JvmStatic
    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @JvmStatic
    @TypeConverter
    fun fromPriority(priority: Priority?): String? {
        return priority?.name
    }

    @JvmStatic
    @TypeConverter
    fun toPriority(priority: String?): Priority? {
        return if (priority == null) null else Priority.valueOf(priority)
    }
}