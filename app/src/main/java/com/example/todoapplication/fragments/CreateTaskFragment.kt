package com.example.todoapplication.fragments

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TimePicker
import android.widget.Toast
import com.example.todoapplication.R
import com.example.todoapplication.database.TodoDataBaseHandler
import com.example.todoapplication.model.Todo
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.Calendar

class CreateTaskFragment : BottomSheetDialogFragment() {

    private var listener: OnTaskAddedListener? = null
    private var mYear: Int = 0
    private var mMonth:Int = 0
    private var mDay:Int = 0
    private var datePickerDialog: DatePickerDialog? = null
    private var mHour = 0
    private var mMinute:Int = 0
    private var timePickerDialog: TimePickerDialog? = null

    fun setOnTaskAddedListener(listener: OnTaskAddedListener) {
        this.listener = listener
    }
    @SuppressLint("MissingInflatedId", "ClickableViewAccessibility", "SetTextI18n")
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

        date.setOnTouchListener(OnTouchListener { view: View?, motionEvent: MotionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_UP) {
                val c = Calendar.getInstance()
                mYear = c[Calendar.YEAR]
                mMonth = c[Calendar.MONTH]
                mDay = c[Calendar.DAY_OF_MONTH]
                datePickerDialog = DatePickerDialog(
                    requireActivity(),
                    { _: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                        date.setText(dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)
                        datePickerDialog?.dismiss()
                    }, mYear, mMonth, mDay
                )
                datePickerDialog!!.datePicker.minDate = System.currentTimeMillis() - 1000
                datePickerDialog!!.show()
            }
            true
        })

        time.setOnTouchListener(OnTouchListener { view: View?, motionEvent: MotionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_UP) {
                // Get Current Time
                val c = Calendar.getInstance()
                mHour = c[Calendar.HOUR_OF_DAY]
                mMinute = c[Calendar.MINUTE]

                // Launch Time Picker Dialog
                timePickerDialog = TimePickerDialog(
                    activity,
                    { _: TimePicker?, hourOfDay: Int, minute: Int ->
                        time.setText("$hourOfDay:$minute")
                        timePickerDialog?.dismiss()
                    }, mHour, mMinute, false
                )
                timePickerDialog!!.show()
            }
            true
        })

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
}

interface OnTaskAddedListener {
    fun onTaskAdded()
}