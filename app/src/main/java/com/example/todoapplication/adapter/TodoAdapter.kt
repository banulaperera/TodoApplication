package com.example.myapplication

import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapplication.R
import com.example.todoapplication.model.Todo

class TodoAdapter(
    private val todos: MutableList<Todo>

) : RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        return TodoViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_task,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return todos.size
    }

    fun addTodo(todo: Todo) {
        todos.add(todo)
        notifyItemInserted(todos.size - 1)
    }

    fun deleteDoneTodos() {
        todos.removeAll { todo ->
            todo.isChecked
        }
        notifyDataSetChanged()
    }

    private fun toggleStrikeThrough(todoTitle: TextView, description: TextView, isChecked: Boolean) {
        if (isChecked) {
            todoTitle.paintFlags = todoTitle.paintFlags or STRIKE_THRU_TEXT_FLAG
            description.paintFlags = description.paintFlags or STRIKE_THRU_TEXT_FLAG
        } else {
            todoTitle.paintFlags = todoTitle.paintFlags and STRIKE_THRU_TEXT_FLAG.inv()
            description.paintFlags = description.paintFlags and STRIKE_THRU_TEXT_FLAG.inv()
        }
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val curTodo = todos[position]
        holder.itemView.apply {
            val todoTitle = findViewById<TextView>(R.id.title)
            todoTitle.text = curTodo.title
            val description = findViewById<TextView>(R.id.description)
            description.text = curTodo.description
            val done = findViewById<CheckBox>(R.id.checkBox)
            done.isChecked = curTodo.isChecked
            toggleStrikeThrough(todoTitle, description, curTodo.isChecked)
            done.setOnCheckedChangeListener { _, isChecked ->
                toggleStrikeThrough(todoTitle, description, isChecked)
                curTodo.isChecked = !curTodo.isChecked
            }
        }

    }
}