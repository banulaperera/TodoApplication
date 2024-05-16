package com.example.todoapplication.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.todoapplication.R
import com.example.todoapplication.database.TodoDataBaseHandler
import com.example.todoapplication.model.Todo
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CreateTaskFragment : BottomSheetDialogFragment() {

    private var listener: OnTaskAddedListener? = null

    fun setOnTaskAddedListener(listener: OnTaskAddedListener) {
        this.listener = listener
    }
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_create_task, container, false)

        val addTask = view.findViewById<Button>(R.id.addTask)
        val title = view.findViewById<EditText>(R.id.addTaskTitle)
        val description = view.findViewById<EditText>(R.id.addTaskDescription)
        val date = view.findViewById<EditText>(R.id.taskDate)
        val time = view.findViewById<EditText>(R.id.taskTime)


        addTask.setOnClickListener {
            if (title.text.isNotEmpty() && description.text.isNotEmpty() && date.text.isNotEmpty() && time.text.isNotEmpty()) {
                val userId = arguments?.getInt("userId")
                print(userId)
                val todo = Todo(
                    title.text.toString(),
                    description.text.toString(),
                    date.text.toString(),
                    time.text.toString(),
                    false,
                    userId!!
                )
                val db = TodoDataBaseHandler(requireContext())
                db.insertTodoData(todo)
                listener?.onTaskAdded()
                dismiss()
            } else {
                Toast.makeText(context, "Please fill out all fields", Toast.LENGTH_SHORT).show()
                dismiss()
            }
        }

        return view
    }

    interface OnTaskAddedListener {
        fun onTaskAdded()
    }
}