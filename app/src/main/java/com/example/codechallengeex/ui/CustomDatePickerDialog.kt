package com.example.codechallengeex.ui

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import com.example.codechallengeex.data.interfaces.DateTimeInterface
import com.example.codechallengeex.utils.Constants
import java.util.*

/**
 * Created by Satya Seshu on 09/04/22.
 */
class CustomDatePickerDialog : DialogFragment(), DatePickerDialog.OnDateSetListener {

    private var dateTimeInterface: DateTimeInterface? = null
    private var calendarMinMaxType = -1
    private var maxDateCalendar: Calendar = Calendar.getInstance()
    private var minDateCalendar: Calendar = Calendar.getInstance()

    fun setCallBack(pDateTimeInterface: DateTimeInterface?) {
        dateTimeInterface = pDateTimeInterface
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        minDateCalendar = Calendar.getInstance()
        maxDateCalendar = Calendar.getInstance()

        var year: Int = -1
        var month: Int = -1
        var day: Int = -1
        val extras = arguments
        if (extras != null) {
            calendarMinMaxType = extras.getInt(Constants.KEY_CALENDAR_MIN_MAX_TYPE)
            year = extras.getInt(Constants.KEY_YEAR)
            month = extras.getInt(Constants.KEY_MONTH)
            day = extras.getInt(Constants.KEY_DAY_OF_MONTH)
        }
        val picker = DatePickerDialog(requireActivity(), this, year, month, day)
        when (calendarMinMaxType) {
            Constants.CALENDAR_MAX_TYPE -> {
                val maxCalendar = Calendar.getInstance()
                picker.datePicker.maxDate = maxCalendar.timeInMillis
            }
            Constants.CALENDAR_TO_DATE_TYPE -> {
                val minCalendar = extras?.getSerializable(Constants.KEY_MIN_CALENDAR) as Calendar
                val maxCalendar = extras.getSerializable(Constants.KEY_MAX_CALENDAR) as Calendar
                picker.datePicker.minDate = minCalendar.time.time
                picker.datePicker.maxDate = maxCalendar.time.time
            }
        }
        return picker
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        dateTimeInterface?.onDateSet(year, month, dayOfMonth)
    }
}