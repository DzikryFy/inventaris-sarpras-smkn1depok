package com.example.inventarissarprassmkn1depok.ui.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateFormatterHelper {
    private val localeId = Locale.forLanguageTag("id-ID")

    /**
     * Memformat tanggal lengkap beserta jam ke format Indonesia,
     * contoh: "2026-11-21 08:48" -> "21 Nov 2026, 08:48"
     * atau "2026-11-21" -> "21 Nov 2026"
     */
    fun formatIndonesianDateTime(rawDate: String?): String {
        if (rawDate.isNullOrBlank()) return "-"
        val trimmed = rawDate.trim()
        return try {
            if (trimmed.length > 10) {
                val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                val outputFormat = SimpleDateFormat("d MMM yyyy, HH:mm", localeId)
                val parsed = inputFormat.parse(trimmed)
                if (parsed != null) outputFormat.format(parsed) else trimmed
            } else {
                val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val outputFormat = SimpleDateFormat("d MMM yyyy", localeId)
                val parsed = inputFormat.parse(trimmed)
                if (parsed != null) outputFormat.format(parsed) else trimmed
            }
        } catch (e: Exception) {
            trimmed
        }
    }

    /**
     * Memformat tanggal hanya hari, bulan, dan tahun ke format Indonesia ringkas,
     * contoh: "2026-11-21 08:48" atau "2026-11-21" -> "21 Nov 2026"
     */
    fun formatIndonesianDateShort(rawDate: String?): String {
        if (rawDate.isNullOrBlank()) return "-"
        val trimmed = rawDate.trim()
        return try {
            val datePart = if (trimmed.length >= 10) trimmed.take(10) else trimmed
            val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val outputFormat = SimpleDateFormat("d MMM yyyy", localeId)
            val parsed = inputFormat.parse(datePart)
            if (parsed != null) outputFormat.format(parsed) else trimmed
        } catch (e: Exception) {
            trimmed
        }
    }
}
