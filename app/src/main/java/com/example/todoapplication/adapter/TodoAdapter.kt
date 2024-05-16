package com.example.todoapplication.adapter

import android.content.Context
import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapplication.R
import com.example.todoapplication.database.TodoDataBaseHandler
import com.example.todoapplication.model.Todo
import java.text.SimpleDateFormat
import java.util.Locale

class TodoAdapter(
    private val context: Context,
    private val todos: MutableList<Todo>
) : RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    private var dateFormat: SimpleDateFormat = SimpleDateFormat("EE dd MMM yyyy", Locale.US)
    private var inputDateFormat: SimpleDateFormat = SimpleDateFormat("dd-M-yyyy", Locale.US)

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

    private fun toggleStrikeThrough(
        todoTitle: TextView,
        description: TextView,
        time: TextView,
        date: TextView,
        day: TextView,
        month: TextView,
        isChecked: Boolean
    ) {
        if (isChecked) {
            todoTitle.paintFlags = todoTitle.paintFlags or STRIKE_THRU_TEXT_FLAG
            description.paintFlags = description.paintFlags or STRIKE_THRU_TEXT_FLAG
            time.paintFlags = time.paintFlags or STRIKE_THRU_TEXT_FLAG
            date.paintFlags = date.paintFlags or STRIKE_THRU_TEXT_FLAG
            day.paintFlags = day.paintFlags or STRIKE_THRU_TEXT_FLAG
            month.paintFlags = month.paintFlags or STRIKE_THRU_TEXT_FLAG
        } else {
            todoTitle.paintFlags = todoTitle.paintFlags and STRIKE_THRU_TEXT_FLAG.inv()
            description.paintFlags = description.paintFlags and STRIKE_THRU_TEXT_FLAG.inv()
            time.paintFlags = time.paintFlags and STRIKE_THRU_TEXT_FLAG.inv()
            date.paintFlags = date.paintFlags and STRIKE_THRU_TEXT_FLAG.inv()
            day.paintFlags = day.paintFlags and STRIKE_THRU_TEXT_FLAG.inv()
            month.paintFlags = month.paintFlags and STRIKE_THRU_TEXT_FLAG.inv()
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
            val time = findViewById<TextView>(R.id.time)
            time.text = curTodo.time
            val menu = findViewById<ImageView>(R.id.options)
            menu.setOnClickListener {
                showPopupMenu(it, position)
            }

            val dateText = findViewById<TextView>(R.id.date)
            val dayText = findViewById<TextView>(R.id.day)
            val monthText = findViewById<TextView>(R.id.month)

            try {
                val date = inputDateFormat.parse(curTodo.date)
                val outputDateString = date?.let { dateFormat.format(it) }

                val items1: Array<String> =
                    outputDateString?.split(" ".toRegex())?.dropLastWhile { it.isEmpty() }
                        ?.toTypedArray() ?: arrayOf("")
                val day = items1[0]
                val dd = items1[1]
                val month = items1[2]

                dateText.text = dd
                dayText.text = day
                monthText.text = month
            } catch (e: Exception) {
                Toast.makeText(context, "Error in date format", Toast.LENGTH_SHORT).show()
            }

            toggleStrikeThrough(
                todoTitle,
                description,
                time,
                dateText,
                dayText,
                monthText,
                curTodo.isChecked
            )
            done.setOnCheckedChangeListener { _, isChecked ->
                toggleStrikeThrough(
                    todoTitle,
                    description,
                    time,
                    dateText,
                    dayText,
                    monthText,
                    isChecked
                )
                curTodo.isChecked = !curTodo.isChecked
            }
        }
    }

    private fun showPopupMenu(view: View, position: Int) {
        val popupMenu = PopupMenu(view.context, view)
        val inflater: MenuInflater = popupMenu.menuInflater
        inflater.inflate(R.menu.delete_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menuDelete -> {
                    deleteTask(position)
                    true
                }

                else -> false
            }
        }
        popupMenu.show()
    }


    private fun deleteTask(position: Int) {
        TodoDataBaseHandler(context).deleteTodoData(todos[position].id)
        todos.removeAt(position)
        notifyItemRemoved(position)
    }
}