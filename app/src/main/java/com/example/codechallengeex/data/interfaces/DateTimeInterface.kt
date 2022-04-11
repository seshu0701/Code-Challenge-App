package com.example.codechallengeex.data.interfaces

/**
 * Created by Satya Seshu on 10/04/22.
 */
interface DateTimeInterface {

    fun onTimeSet(hourOfDay: Int, minute: Int, amOrPm: String)

    fun onDateSet(year: Int, month: Int, dayOfMonth: Int)
}