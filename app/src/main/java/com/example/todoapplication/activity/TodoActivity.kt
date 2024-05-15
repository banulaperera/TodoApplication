package com.example.todoapplication.activity

import android.os.Bundle
import android.view.MenuInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import com.example.todoapplication.R
import com.example.todoapplication.fragments.CreateTaskFragment

class TodoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo)

        val addTask = findViewById<TextView>(R.id.addTask)
        val profileImage = findViewById<ImageView>(R.id.profile)
        profileImage.setOnClickListener {
            showPopupMenu(it)
        }
        addTask.setOnClickListener {
            val createTaskFragment = CreateTaskFragment()
            createTaskFragment.show(supportFragmentManager, "create_task")
        }
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