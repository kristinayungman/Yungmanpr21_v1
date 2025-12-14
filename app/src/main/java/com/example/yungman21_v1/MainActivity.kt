package com.example.yungman21_v1

import android.annotation.SuppressLint
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MainActivity : AppCompatActivity() {

    private lateinit var sharedPreferences : SharedPreferences
    private lateinit var scrollView: ScrollView
    private lateinit var item : LinearLayout

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE)

        var currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)

        var savedLogin = sharedPreferences.getString("user_login", null)
        var savedPassword = sharedPreferences.getString("user_password", null)

        var login = Gson().fromJson(savedLogin, String::class.java)
        var password = Gson().fromJson(savedPassword, String::class.java)

        if (currentFragment == null)
        {
            if (login == null && password == null)
            {

                val loginFragment = LoginFragment()

                supportFragmentManager
                    .beginTransaction()
                    .add(R.id.fragment_container, loginFragment)
                    .commit()
            }
            else if (login == "ects" && password == "ects2025")
            {
                val ticketsFragment = TicketsFragment()

                supportFragmentManager
                    .beginTransaction()
                    .add(R.id.fragment_container, ticketsFragment)
                    .commit()
            }
        }


    }
}