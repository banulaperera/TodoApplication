package com.example.todoapplication.database

import android.annotation.SuppressLint
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.todoapplication.model.Todo

class TodoDataBaseHandler(context: Context) : SQLiteOpenHelper(context, "Todos", null, 1) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("""
            CREATE TABLE Todos (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                user_id INTEGER,
                title TEXT,
                description TEXT,
                date TEXT,
                time TEXT,
                priority TEXT,
                isChecked BOOLEAN,
                FOREIGN KEY(user_id) REFERENCES Users(id)
            )
        """.trimIndent())
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS Todos")
        onCreate(db)
    }

    fun insertTodoData(todo: Todo) {
        val db = this.writableDatabase
        db.execSQL("""
            INSERT INTO Todos (
                user_id,
                title,
                description,
                date,
                time,
                priority,
                isChecked
            ) VALUES (
                ${todo.userId},
                '${todo.title}',
                '${todo.description}',
                '${todo.date}',
                '${todo.time}',
                '${todo.priority}',
                ${todo.isChecked}
            )
        """.trimIndent())
        db.close()
    }

    @SuppressLint("Range")
    fun readTodoData(userId: Int): MutableList<Todo> {
        val list: MutableList<Todo> = ArrayList()
        val db = this.readableDatabase
        val result = db.rawQuery("SELECT * FROM Todos WHERE user_id = $userId", null)

        // Loop through the result set
        if (result.moveToFirst()) {
            do {
                val todo = Todo().apply {
                    id = result.getString(result.getColumnIndex("id")).toInt()
                    this.userId = result.getString(result.getColumnIndex("user_id")).toInt()
                    title = result.getString(result.getColumnIndex("title"))
                    description = result.getString(result.getColumnIndex("description"))
                    date = result.getString(result.getColumnIndex("date"))
                    time = result.getString(result.getColumnIndex("time"))
                    priority = result.getString(result.getColumnIndex("priority"))
                    isChecked = result.getString(result.getColumnIndex("isChecked")).toBoolean()
                }
                list.add(todo)
            } while (result.moveToNext())
        }
        result.close()
        db.close()
        return list
    }

    fun updateTodoData(todo: Todo) {
        val db = this.writableDatabase
        db.execSQL("""
            UPDATE Todos SET
                title = '${todo.title}',
                description = '${todo.description}',
                date = '${todo.date}',
                time = '${todo.time}',
                priority = '${todo.priority}',
                isChecked = ${todo.isChecked}
            WHERE id = ${todo.id}
        """.trimIndent())
        db.close()
    }

    fun deleteTodoData(todo: Int) {
        val db = this.writableDatabase
        db.execSQL("DELETE FROM Todos WHERE id = $todo")
        db.close()
    }
}