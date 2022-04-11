package com.example.codechallengeex.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.codechallengeex.R
import com.example.codechallengeex.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var imagesListFragmentByDate: ImagesListFragmentByDate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        imagesListFragmentByDate = ImagesListFragmentByDate.getInstance()

        val fm = supportFragmentManager
        val ft = fm.beginTransaction()
        ft.replace(
            R.id.layout_container,
            imagesListFragmentByDate,
            ImagesListFragmentByDate::class.java.name
        )
        ft.commit()
    }

    override fun onBackPressed() {
        imagesListFragmentByDate.updateCache()
        super.onBackPressed()
    }
}