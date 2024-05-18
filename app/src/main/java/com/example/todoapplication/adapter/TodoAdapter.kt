package com.example.todoapplication.adapter

import android.content.Context
import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.os.Build
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapplication.R
import com.example.todoapplication.database.TodoDataBaseHandler
import com.example.todoapplication.model.Todo
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val curTodo = todos[position]
        holder.itemView.apply {
            val todoTitle = findViewById<TextView>(R.id.title)
            todoTitle.text = curTodo.title
            val description = findViewById<TextView>(R.id.description)
            description.text = curTodo.description
            val complete = findViewById<TextView>(R.id.status)
            complete.text = check(curTodo, complete)
            val time = findViewById<TextView>(R.id.time)
            time.text = curTodo.time
            val priority = findViewById<TextView>(R.id.priority)
            priority.text = curTodo.priority

            val priorityColor = when (curTodo.priority) {
                "High" -> R.color.red
                "Medium" -> R.color.yellow
                "Low" -> R.color.green
                else -> R.color.secondary_text
            }

            priority.setTextColor(context.resources.getColor(priorityColor, null))
            val menu = findViewById<ImageView>(R.id.options)
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
            menu.setOnClickListener {
                showPopupMenu(
                    it,
                    position,
                    todoTitle,
                    description,
                    time,
                    dateText,
                    dayText,
                    monthText,
                    curTodo
                )
            }
        }
    }

    private fun showPopupMenu(
        view: View,
        position: Int,
        todoTitle: TextView,
        description: TextView,
        time: TextView,
        dateText: TextView,
        dayText: TextView,
        monthText: TextView,
        curTodo: Todo
    ) {
        val popupMenu = PopupMenu(view.context, view)
        val inflater: MenuInflater = popupMenu.menuInflater
        inflater.inflate(R.menu.delete_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {

                R.id.menuDelete -> {
                    AlertDialog.Builder(context)
                        .setTitle("Delete Task")
                        .setMessage("Are you sure you want to delete this task?")
                        .setPositiveButton("Yes") { _, _ ->
                            deleteTask(position)
                        }
                        .setNegativeButton("No") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .create()
                        .show()
                    true
                }

                R.id.menuComplete -> {
                    toggleStrikeThrough(
                        todoTitle,
                        description,
                        time,
                        dateText,
                        dayText,
                        monthText,
                        curTodo.isChecked
                    )
                    curTodo.isChecked = !curTodo.isChecked
                    todos[position] = curTodo
                    updateTask(position)
                    true
                }

                else -> false
            }
        }
        popupMenu.show()
    }

    private fun updateTask(position: Int) {
        if (position < todos.size) {
            TodoDataBaseHandler(context).updateTodoData(todos[position])
            notifyItemChanged(position)
        }
    }

    private fun deleteTask(position: Int) {
        if (position < todos.size) {
            TodoDataBaseHandler(context).deleteTodoData(todos[position].id)
            todos.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun check(todo: Todo, textView: TextView): String {
        val formatter = DateTimeFormatter.ofPattern("dd-M-yyyy H:m")
        val scheduledDateTime = LocalDateTime.parse("${todo.date} ${todo.time}", formatter)
        var status = when {
            LocalDateTime.now().isAfter(scheduledDateTime) -> "Overdue"
            todo.isChecked -> "Completed"
            else -> "Pending"
        }

        if (status == "Overdue" || todo.isChecked) {
            status = if (todo.isChecked) {
                "Completed"
            } else "Overdue"
        }

        if (status == "Pending" || todo.isChecked) {
            status = if (todo.isChecked) "Completed" else "Pending"
        }

        val color = when (status) {
            "Overdue" -> R.color.red
            else -> R.color.secondary_text
        }

        textView.setTextColor(context.resources.getColor(color, null))

        return status
    }
}
