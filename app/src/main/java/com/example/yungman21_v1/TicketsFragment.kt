package com.example.yungman21_v1

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStreamReader
import com.google.android.material.snackbar.Snackbar
// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TicketsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TicketsFragment : Fragment() {

    private lateinit var item: LinearLayout
    private lateinit var buttonBack: Button
    private lateinit var buttonClear: Button
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_tickets, container, false)

        buttonBack = view.findViewById(R.id.buttonBack)
        buttonClear = view.findViewById(R.id.buttonClear)
        item = view.findViewById(R.id.layout)
        sharedPreferences = requireContext().getSharedPreferences("myPrefs", MODE_PRIVATE)

        // === Кнопка "Назад" ===
        buttonBack.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, LoginFragment())
                .commit()
        }

        // === Кнопка "Очистить" ===
        buttonClear.setOnClickListener {
            sharedPreferences.edit {
                remove("ticketList")
                remove("user_login")
                remove("user_password")
            }
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, LoginFragment())
                .commit()
        }

        // === Загрузка билетов ===
        val tickets = loadTickets()

        // === Сохранение при изменениях ===
        fun saveTickets() {
            val json = Gson().toJson(tickets)
            sharedPreferences.edit { putString("ticketList", json) }
        }

        // === Функция обновления UI всех билетов ===
        // === Функция обновления UI всех билетов ===
        fun updateUI() {
            item.removeAllViews()
            var correctCount = 0
            var answeredCount = 0

            for (ticket in tickets) {
                val itemView = LayoutInflater.from(context)
                    .inflate(R.layout.list_item, item, false)

                val textNumber = itemView.findViewById<TextView>(R.id.ticketText)
                val textScore = itemView.findViewById<TextView>(R.id.scoreText)
                val passedCheckBox = itemView.findViewById<CheckBox>(R.id.passedCheck)

                textNumber.text = "Билет №${ticket.number}"
                textScore.text = if (ticket.passed) {
                    "Ответ: ${if (ticket.userAnswer == true) "Да" else "Нет"}"
                } else {
                    "Не пройден"
                }

                passedCheckBox.isChecked = ticket.passed
                passedCheckBox.isClickable = false

                when (ticket.isCorrect) {
                    true -> {
                        passedCheckBox.setTextColor(Color.GREEN)
                        correctCount++
                        answeredCount++
                    }
                    false -> {
                        passedCheckBox.setTextColor(Color.RED)
                        answeredCount++
                    }
                    null -> passedCheckBox.setTextColor(Color.GRAY)
                }

                itemView.setOnClickListener {
                    if (ticket.passed) {
                        Snackbar.make(view!!, "Билет уже пройден", Snackbar.LENGTH_SHORT).show()
                    } else {
                        AlertDialog.Builder(requireContext())
                            .setTitle("Билет №${ticket.number}")
                            .setMessage(ticket.question)
                            .setPositiveButton("Да") { _, _ ->
                                ticket.userAnswer = true
                                updateUI()
                                saveTickets()
                            }
                            .setNegativeButton("Нет") { _, _ ->
                                ticket.userAnswer = false
                                updateUI()
                                saveTickets()
                            }
                            .setCancelable(true)
                            .show()
                    }
                }

                item.addView(itemView)
            }

            // Update the summary text
            val summaryText = "$correctCount / ${tickets.size}"
            view?.findViewById<TextView>(R.id.scoreSummary)?.text = summaryText
        }

        // === Первичный рендер ===
        updateUI()

        return view
    }


    private fun loadTickets(): MutableList<Ticket> {
        val gson = Gson()
        val type = object : TypeToken<MutableList<Ticket>>() {}.type

        val savedJson = sharedPreferences.getString("ticketList", null)
        if (savedJson != null) {
            return try {
                gson.fromJson(savedJson, type)
            } catch (e: Exception) {

                loadFromRaw()
            }
        }
        return loadFromRaw()
    }

    // === Загрузка из res/raw/tickets.json ===
    private fun loadFromRaw(): MutableList<Ticket> {
        val context = requireContext()
        val inputStream = context.resources.openRawResource(R.raw.tickets)
        val reader = InputStreamReader(inputStream, Charsets.UTF_8)
        val json = reader.readText()
        reader.close()
        inputStream.close()

        val gson = Gson()
        val type = object : TypeToken<List<Ticket>>() {}.type
        val tickets = gson.fromJson<List<Ticket>>(json, type).toMutableList()

        // Важно: при первом запуске userAnswer = null
        for (t in tickets) {
            t.userAnswer = null
        }

        return tickets
    }
}