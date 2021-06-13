package com.mashkov.todoister.util

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.mashkov.todoister.model.Priority
import com.mashkov.todoister.model.Task
import java.text.SimpleDateFormat
import java.util.*

object Utils {
    fun formatDate(date: Date): String {
        val simpleDateFormat = SimpleDateFormat.getDateInstance() as SimpleDateFormat
        simpleDateFormat.applyPattern("EEE, MMM d")
        return simpleDateFormat.format(date)
    }

    @JvmStatic
    fun hideSoftKeyboard(view: View) {
        val imm = view.context.getSystemService(
                Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun priorityColor(task: Task): Int {
        val color: Int
        color = if (task.getPriority() === Priority.HIGH) {
            Color.argb(200, 201, 21, 23)
        } else if (task.getPriority() === Priority.MEDIUM) {
            Color.argb(200, 225, 204, 50)
        } else {
            Color.argb(200, 20, 181, 190)
        }
        return color
    }
}