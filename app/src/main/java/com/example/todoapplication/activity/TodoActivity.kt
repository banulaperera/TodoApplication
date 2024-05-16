package com.example.todoapplication.activity

import android.content.Intent
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
import com.example.todoapplication.fragments.OnTaskAddedListener

class TodoActivity : AppCompatActivity(), OnTaskAddedListener {
    private lateinit var todoAdapter: TodoAdapter
    private var userId: Int = -1
    private lateinit var todoItems: RecyclerView
    private lateinit var addTask: TextView
    private lateinit var profileImage: ImageView
    private val todoDataBaseHandler = TodoDataBaseHandler(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo)

        userId = intent.getIntExtra("userId", -1)
        todoItems = findViewById(R.id.taskRecycler)
        addTask = findViewById(R.id.addTask)
        profileImage = findViewById(R.id.profile)

        profileImage.setOnClickListener {
            showPopupMenu(it)
        }

        addTask.setOnClickListener {
            val createTaskFragment = CreateTaskFragment()
            val bundle = Bundle()
            bundle.putInt("userId", userId)
            createTaskFragment.arguments = bundle
            createTaskFragment.setOnTaskAddedListener(this)
            createTaskFragment.show(supportFragmentManager, "create_task")
        }

        onTaskAdded()
    }

    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(this, view)
        val inflater: MenuInflater = popupMenu.menuInflater
        inflater.inflate(R.menu.menu_logout, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.logout -> {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    true
                }

                else -> false
            }
        }
        popupMenu.show()
    }

    override fun onTaskAdded() {
        val data = todoDataBaseHandler.readTodoData(userId)
        todoAdapter = TodoAdapter(this, data)
        todoItems.adapter = todoAdapter
        todoItems.layoutManager = LinearLayoutManager(this)
    }

}