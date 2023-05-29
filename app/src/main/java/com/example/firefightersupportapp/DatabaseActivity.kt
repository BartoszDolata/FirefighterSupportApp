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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_database)
        database = DBHelper(this, null)
        renderView()
    }

    private fun renderView() {
        val firefighters = getData()

        val table = this@DatabaseActivity.findViewById(R.id.table) as TableLayout
        for (firefighter in firefighters) {
            val tr = TableRow(this)
            tr.layoutParams = TableRow.LayoutParams(
                TableRow.LayoutParams.FILL_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT
            )

            val name = TextView(this)
            name.text = firefighter[0]
            name.gravity = Gravity.LEFT
            name.textSize = 15.0F
            name.setPadding(5)
            tr.addView(name)

            val surname = TextView(this)
            surname.text = firefighter[1]
            surname.gravity = Gravity.CENTER_HORIZONTAL
            tr.addView(surname)

            val nick = TextView(this)
            nick.text = firefighter[2]
            nick.gravity = Gravity.RIGHT
            tr.addView(nick)

            table.addView(
                tr,
                TableLayout.LayoutParams(
                    TableLayout.LayoutParams.FILL_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT
                )
            )
        }
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
                Toast.makeText(this@DatabaseActivity, nick, Toast.LENGTH_SHORT).show()
                Toast.makeText(this@DatabaseActivity, "Dodano pomyślnie", Toast.LENGTH_SHORT).show()
            }
            popupWindow.dismiss()
            renderView()
        }
    }

}