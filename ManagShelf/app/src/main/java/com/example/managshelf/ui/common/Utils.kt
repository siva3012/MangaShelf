package com.example.managshelf.ui.common

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object Utils {

    fun timestampToYear(timestamp: Long): Int {
        val date = Date(timestamp * 1000)
        val calendar = Calendar.getInstance()
        calendar.time = date
        return calendar.get(Calendar.YEAR)
    }
    fun timestampToReadableDate(timestamp: Long): String {
        val date = Date(timestamp * 1000)
        val format = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        return format.format(date)
    }
}