package com.example.firefightersupportapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.setPadding


class DatabaseActivity : AppCompatActivity() {
    private lateinit var database : DBHelper
    private lateinit var table : TableLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_database)
        database = DBHelper(this, null)
        table = findViewById(R.id.table)
        renderView()
    }

    private fun renderView() {
        val firefighters = getData()
        for (firefighter in firefighters) {
            val tr = TableRow(this)

            val name = TextView(this)
            name.text = firefighter[0]
            name.textSize = 15.0F
            name.setPadding(5)

            val surname = TextView(this)
            surname.text = firefighter[1]


            val nick = TextView(this)
            nick.text = firefighter[2]


            tr.addView(name)
            tr.addView(surname)
            tr.addView(nick)

            // Ustaw szerokość kolumn
            val weightSum = 3.0f // Suma wag kolumn
            val params = TableRow.LayoutParams(
                0,
                TableRow.LayoutParams.WRAP_CONTENT,
                1.0f / weightSum
            )
            name.layoutParams = params
            surname.layoutParams = params
            nick.layoutParams = params

            table.addView(tr)
        }

        table.requestLayout()
    }

    private fun renderRow(name: String, surname: String, nick: String) {
        val tr = TableRow(this)

        val nameTv = TextView(this)
        nameTv.text = name
        nameTv.textSize = 15.0F
        nameTv.setPadding(5)

        val surnameTv = TextView(this)
        surnameTv.text = surname

        val nickTv = TextView(this)
        nickTv.text = nick

        // Ustaw szerokość kolumn
        val weightSum = 3.0f // Suma wag kolumn
        val params = TableRow.LayoutParams(
            0,
            TableRow.LayoutParams.WRAP_CONTENT,
            1.0f / weightSum
        )
        nameTv.layoutParams = params
        surnameTv.layoutParams = params
        nickTv.layoutParams = params

        tr.addView(nameTv)
        tr.addView(surnameTv)
        tr.addView(nickTv)

        table.addView(tr)
        table.requestLayout()
    }

    @SuppressLint("Range")
    private fun getData(): MutableList<List<String>> {
        val cursor = database.getFirefighters()
        val data = mutableListOf<List<String>>()

        if (cursor!!.moveToFirst()) {
            do {
                val firefighter = mutableListOf<String>()
                firefighter.add(cursor.getString(cursor.getColumnIndex(DBHelper.NAME)))
                firefighter.add(cursor.getString(cursor.getColumnIndex(DBHelper.SURNAME)))
                firefighter.add(cursor.getString(cursor.getColumnIndex(DBHelper.NICK)))
                data.add(firefighter)
            } while (cursor.moveToNext())
        }

        return data
    }

    fun onClickDone(view: View?) {
        val inflater: LayoutInflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView: View = inflater.inflate(R.layout.popup_window, null)

        val width: Int = LinearLayout.LayoutParams.WRAP_CONTENT
        val height: Int = LinearLayout.LayoutParams.WRAP_CONTENT
        val focusable = true

        val popupWindow = PopupWindow(popupView, width, height, focusable)
        popupWindow.elevation = 20F
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)

        val saveButton = popupView.findViewById<Button>(R.id.ok_button)
        saveButton.setOnClickListener {

            val name = popupView.findViewById<EditText>(R.id.et_name).text.toString()
            val surname = popupView.findViewById<EditText>(R.id.et_surname).text.toString()
            val nick = popupView.findViewById<EditText>(R.id.et_nick).text.toString()

            print("### $name $surname $nick")
            val added = database.addFirefighter(name, surname, nick)
            if (added > 0) {
                Toast.makeText(this@DatabaseActivity, "Dodano pomyślnie", Toast.LENGTH_SHORT).show()
            }
            popupWindow.dismiss()

            renderRow(name, surname, nick)
        }
    }

}