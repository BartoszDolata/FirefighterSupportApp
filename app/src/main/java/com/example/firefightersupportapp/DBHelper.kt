package com.example.firefightersupportapp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        val query = ("CREATE TABLE " + TABLE_NAME + " ("
                + ID + " INTEGER PRIMARY KEY, " +
                NAME + " TEXT," +
                SURNAME + " TEXT," +
                NICK + " TEXT" + ")")

        db!!.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addFirefighter(name : String, surname : String, nick : String){
        val values = ContentValues()
        values.put(NAME, name)
        values.put(SURNAME, surname)
        values.put(NICK, nick)
        val db = this.writableDatabase
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun getFirefighters(): Cursor? {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_NAME", null)

    }

    companion object{
        private val DATABASE_NAME = "FIREFIGHTERS"
        private val DATABASE_VERSION = 1
        val TABLE_NAME = "firefighters_data"
        val ID = "id"
        val NAME = "name"
        val SURNAME = "surname"
        val NICK = "nick"
    }
}