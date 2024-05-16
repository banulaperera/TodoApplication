package com.example.todoapplication.database

import android.annotation.SuppressLint
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.todoapplication.model.User

const val DATABASE_NAME = "MyDB"
const val TABLE_NAME = "Users"
const val COLUMN_EMAIL = "email"
const val COLUMN_PASSWORD = "password"
const val COLUMN_ID = "id"

class UserDataBaseHandler(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_EMAIL TEXT, $COLUMN_PASSWORD TEXT)"
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertUserData(user: User) {
        val db = this.writableDatabase
        val query = "INSERT INTO $TABLE_NAME ($COLUMN_EMAIL, $COLUMN_PASSWORD) VALUES ('${user.email}', '${user.password}')"
        db.execSQL(query)
        db.close()
    }

    @SuppressLint("Range")
    fun readUserData(): MutableList<User> {
        val list: MutableList<User> = ArrayList()
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME"
        val result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
            do {
                val user = User()
                user.id = result.getString(result.getColumnIndex(COLUMN_ID)).toInt()
                user.email = result.getString(result.getColumnIndex(COLUMN_EMAIL))
                user.password = result.getString(result.getColumnIndex(COLUMN_PASSWORD))
                list.add(user)
            } while (result.moveToNext())
        }
        result.close()
        db.close()
        return list
    }
}
