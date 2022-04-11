package com.example.codechallengeex.ui

import android.app.Dialog
import android.os.Bundle
import android.text.TextUtils
import android.view.Window
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.codechallengeex.R
import com.example.codechallengeex.data.interfaces.CallBackListener
import com.example.codechallengeex.data.interfaces.DateTimeInterface
import com.example.codechallengeex.databinding.CustomDateSelectionViewBinding
import com.example.codechallengeex.model.ContentModel
import com.example.codechallengeex.utils.Constants
import com.example.codechallengeex.utils.DateUtilities
import java.util.*

/**
 * Created by Satya Seshu on 10/04/22.
 */
class CustomDateSelectionDialog : DialogFragment() {

    private lateinit var binding: CustomDateSelectionViewBinding
    private val fromDateCalendar = Calendar.getInstance()
    private val toDateCalendar = Calendar.getInstance()
    private var fromDateSelected: String? = null
    private var toDateSelected: String? = null
    private var callBackListener: CallBackListener? = null

    companion object {
        fun getInstance(): CustomDateSelectionDialog {
            return CustomDateSelectionDialog()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)
        val dialog = Dialog(requireActivity(), R.style.Theme_Custom_Dialog_One)
        binding = CustomDateSelectionViewBinding.inflate(layoutInflater)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setContentView(binding.root)

        loadUIComponents()
        return dialog
    }

    private fun loadUIComponents() {
        binding.tvStartDate.setOnClickListener {
            startDateSelected()
        }
        binding.tvEndDate.setOnClickListener {
            endDateSelected()
        }

        binding.btnSubmit.setOnClickListener {
            submitSelected()
        }

        fromDateCalendar.set(Calendar.DAY_OF_MONTH, 1)
        fromDateSelected = DateUtilities.getStringDate(fromDateCalendar)
        binding.tvStartDate.text = fromDateSelected

        toDateSelected = DateUtilities.getStringDate(toDateCalendar)
        binding.tvEndDate.text = toDateSelected
    }

    fun setCallBackListener(callBackListener: CallBackListener?) {
        this.callBackListener = callBackListener
    }

    private fun startDateSelected() {
        val datePickerDialog = CustomDatePickerDialog()
        val extras = Bundle()
        extras.putInt(Constants.KEY_YEAR, fromDateCalendar.get(Calendar.YEAR))
        extras.putInt(Constants.KEY_MONTH, fromDateCalendar.get(Calendar.MONTH))
        extras.putInt(
            Constants.KEY_DAY_OF_MONTH,
            fromDateCalendar.get(Calendar.DAY_OF_MONTH)
        )
        extras.putInt(Constants.KEY_CALENDAR_MIN_MAX_TYPE, Constants.CALENDAR_MAX_TYPE)
        datePickerDialog.arguments = extras
        datePickerDialog.setCallBack(object : DateTimeInterface {

            override fun onTimeSet(hourOfDay: Int, minute: Int, amOrPm: String) {

            }

            override fun onDateSet(year: Int, month: Int, dayOfMonth: Int) {
                fromDateCalendar.set(Calendar.YEAR, year)
                fromDateCalendar.set(Calendar.MONTH, month)
                fromDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                fromDateSelected = DateUtilities.getStringDate(fromDateCalendar)
                binding.tvStartDate.text = fromDateSelected

                toDateSelected = ""
                toDateCalendar.set(Calendar.YEAR, fromDateCalendar.get(Calendar.YEAR))
                toDateCalendar.set(Calendar.MONTH, fromDateCalendar.get(Calendar.MONTH))
                toDateCalendar.set(
                    Calendar.DAY_OF_MONTH,
                    fromDateCalendar.get(Calendar.DAY_OF_MONTH)
                )
                binding.tvEndDate.text = ""
            }
        })
        if (!requireActivity().isFinishing) {
            datePickerDialog.show(
                requireActivity().supportFragmentManager,
                CustomDatePickerDialog::class.java.name
            )
        }
    }

    private fun endDateSelected() {
        val datePickerDialog = CustomDatePickerDialog()
        val extras = Bundle()
        extras.putInt(Constants.KEY_YEAR, toDateCalendar.get(Calendar.YEAR))
        extras.putInt(Constants.KEY_MONTH, toDateCalendar.get(Calendar.MONTH))
        extras.putInt(
            Constants.KEY_DAY_OF_MONTH,
            toDateCalendar.get(Calendar.DAY_OF_MONTH)
        )
        extras.putSerializable(Constants.KEY_MIN_CALENDAR, fromDateCalendar)
        extras.putSerializable(Constants.KEY_MAX_CALENDAR, Calendar.getInstance())
        extras.putInt(Constants.KEY_CALENDAR_MIN_MAX_TYPE, Constants.CALENDAR_TO_DATE_TYPE)
        datePickerDialog.arguments = extras
        datePickerDialog.setCallBack(object : DateTimeInterface {

            override fun onTimeSet(hourOfDay: Int, minute: Int, amOrPm: String) {

            }

            override fun onDateSet(year: Int, month: Int, dayOfMonth: Int) {
                toDateCalendar.set(Calendar.YEAR, year)
                toDateCalendar.set(Calendar.MONTH, month)
                toDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                toDateSelected = DateUtilities.getStringDate(toDateCalendar)
                binding.tvEndDate.text = toDateSelected
            }
        })
        if (!requireActivity().isFinishing) {
            datePickerDialog.show(
                requireActivity().supportFragmentManager,
                CustomDatePickerDialog::class.java.name
            )
        }
    }

    private fun submitSelected() {
        if (TextUtils.isEmpty(fromDateSelected)) {
            showErrorMessage(getString(R.string.from_date_cannot_be_empty))
            return
        }
        if (TextUtils.isEmpty(toDateSelected)) {
            showErrorMessage(getString(R.string.to_date_cannot_be_empty))
            return
        }
        val contentModel = ContentModel(fromDateSelected, toDateSelected)
        callBackListener?.callBackSelected(contentModel)
        closeDialog()
    }

    private fun showErrorMessage(message: String?) {
        if (!requireActivity().isFinishing) {
            val alertDialogBuilder = AlertDialog.Builder(requireActivity())
            alertDialogBuilder.setTitle(getString(R.string.dialog_title))
            alertDialogBuilder.setMessage(message)
            alertDialogBuilder.setNeutralButton(getString(R.string.dialog_ok), null)
            alertDialogBuilder.show()
        }
    }

    private fun closeDialog() {
        if (dialog != null) {
            if (dialog?.isShowing == true) {
                dialog?.dismiss()
            }
        }
    }
}