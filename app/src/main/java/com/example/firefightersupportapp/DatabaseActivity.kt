package com.example.firefightersupportapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Gravity
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
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

//        val f1 = listOf("Jan", "Kowalski", "Janek")
//        val f = mutableListOf<List<String>>()
//        f.add(f1)
//
//        return f
    }

}