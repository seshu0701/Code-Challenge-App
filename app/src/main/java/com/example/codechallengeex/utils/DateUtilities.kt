package com.example.codechallengeex.utils

import android.text.TextUtils
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Satya Seshu on 10/04/22.
 */
object DateUtilities {

    fun getCalendarFromTimeStamp(dateValue: String?): String {
        val locale = Locale.getDefault()
        if (TextUtils.isEmpty(dateValue)) {
            return getTimeStampFromCalendar(Calendar.getInstance(locale))
        }
        try {
            val formatter =
                SimpleDateFormat(CALENDAR_DATE_TIME_FORMAT_ONE, locale) // MM/dd/yyyy hh:mm:ss a
            val date = formatter.parse(dateValue!!)
            val cal = Calendar.getInstance(locale)
            cal.time = date!!
            return getTimeStampFromCalendar(cal)
        } catch (ex: Exception) {
            LoggerInfo.errorLog("getCalendarFromTimeStamp", ex.message)
        }
        return getTimeStampFromCalendar(Calendar.getInstance(locale))
    }

    private fun getTimeStampFromCalendar(calendar: Calendar): String {
        val locale = Locale.getDefault()
        return try {
            val formatter =
                SimpleDateFormat(CALENDAR_COMPLETE_DATE_TIME_DAY, locale) // EEE, MMM d, yyyy
            formatter.format(calendar.time)
        } catch (e: Exception) {
            LoggerInfo.errorLog("getTimeStampFromCalendar Exception", e.message)
            getTimeStampFromCalendar(Calendar.getInstance(locale))
        }
    }

    fun getStringDate(calendar: Calendar): String {
        val locale = Locale.getDefault()
        return try {
            val formatter = SimpleDateFormat(CALENDAR_DATE_FORMAT_TWO, locale) // dd
            formatter.format(calendar.time)
        } catch (e: Exception) {
            LoggerInfo.errorLog("getStringDate Exception", e.message)
            getStringDate(Calendar.getInstance(locale))
        }
    }

    fun getDateTimeStampFromCalendar(calendar: Calendar): String {
        val locale = Locale.getDefault()
        return try {
            val formatter =
                SimpleDateFormat(CALENDAR_DATE_TIME_FORMAT_TWO, locale) // EEE, MMM d, yyyy hh:mm a
            formatter.format(calendar.time)
        } catch (e: Exception) {
            LoggerInfo.errorLog("getTimeStampFromCalendar Exception", e.message)
            getTimeStampFromCalendar(Calendar.getInstance(locale))
        }
    }

    private const val CALENDAR_DATE_FORMAT_ONE = "dd/MM/yyyy"
    private const val CALENDAR_DATE_FORMAT_TWO = "yyyy-MM-dd"
    private const val CALENDAR_COMPLETE_DATE_TIME_DAY = "EEE, MMM d, yyyy"
    private const val CALENDAR_DATE_TIME_FORMAT_ONE =
        "yyyy-MM-dd'T'hh:mm:ss'Z'"//2013-05-15T00:00:00Z
    private const val CALENDAR_DATE_TIME_FORMAT_TWO = "EEE, MMM d, yyyy hh:mm a"
}