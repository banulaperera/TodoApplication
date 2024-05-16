package com.example.todoapplication.activity

import android.os.Bundle
import android.view.MenuInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapplication.R
import com.example.todoapplication.adapter.TodoAdapter
import com.example.todoapplication.database.TodoDataBaseHandler
import com.example.todoapplication.fragments.CreateTaskFragment

class TodoActivity : AppCompatActivity() {
    private lateinit var todoAdapter: TodoAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo)
        val todoDataBaseHandler = TodoDataBaseHandler(this)

        val addTask = findViewById<TextView>(R.id.addTask)
        val profileImage = findViewById<ImageView>(R.id.profile)
        profileImage.setOnClickListener {
            showPopupMenu(it)
        }
        addTask.setOnClickListener {
            val createTaskFragment = CreateTaskFragment()
            createTaskFragment.show(supportFragmentManager, "create_task")
        }

        val todoItems = findViewById<RecyclerView>(R.id.taskRecycler)
        val userId = intent.getIntExtra("userId", -1)
        val data = todoDataBaseHandler.readTodoData(userId)
        todoAdapter = TodoAdapter(data)
        todoItems.adapter = todoAdapter
        todoItems.layoutManager = LinearLayoutManager(this)
    }

    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(this, view)
        val inflater: MenuInflater = popupMenu.menuInflater
        inflater.inflate(R.menu.menu_logout, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.logout -> {
                    // Handle logout action here
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }
}