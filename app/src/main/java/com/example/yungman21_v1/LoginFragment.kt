package com.example.yungman21_v1

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import androidx.core.content.edit

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var button : Button
    private lateinit var login : EditText
    private lateinit var pass : EditText
    private lateinit var rememberCheck : CheckBox
    private lateinit var sharedPreferences : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        button = view.findViewById(R.id.buttonEnter)
        login = view.findViewById(R.id.login)
        pass = view.findViewById(R.id.password)
        rememberCheck = view.findViewById(R.id.checkbox)

        sharedPreferences = requireActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE)

        var ticketsFragment = TicketsFragment()

        button.setOnClickListener {
            if (login.text.toString() != "" && pass.text.toString() != "")
            {
                if (login.text.toString() == "ects" && pass.text.toString() == "ects2025")
                {
                    if (rememberCheck.isChecked) {
                        var jsonLogin = Gson().toJson(login.text.toString())
                        var jsonPassword = Gson().toJson(pass.text.toString())

                        sharedPreferences.edit { putString("user_login", jsonLogin) }
                        sharedPreferences.edit { putString("user_password", jsonPassword) }
                    }
                    parentFragmentManager
                        .beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.fragment_container, ticketsFragment)
                        .commit()
                }
                else
                {
                    Snackbar.make(
                        view,
                        "Неверный логин или пароль",
                        Snackbar.LENGTH_SHORT,
                    ).show()
                }
            }
            else
            {
                Snackbar.make(
                    view,
                    "Все поля должны быть заполнены",
                    Snackbar.LENGTH_SHORT,
                ).show()
            }
        }

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LoginFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoginFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}