package com.example.todoapplication.fragments

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TimePicker
import android.widget.Toast
import com.example.todoapplication.R
import com.example.todoapplication.broadcast.AlertReceiver
import com.example.todoapplication.database.TodoDataBaseHandler
import com.example.todoapplication.model.Todo
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CreateTaskFragment : BottomSheetDialogFragment() {

    private var listener: OnTaskAddedListener? = null
    private var mYear: Int = 0
    private var mMonth: Int = 0
    private var mDay: Int = 0
    private var datePickerDialog: DatePickerDialog? = null
    private var mHour = 0
    private var mMinute: Int = 0
    private var timePickerDialog: TimePickerDialog? = null

    fun setOnTaskAddedListener(listener: OnTaskAddedListener) {
        this.listener = listener
    }

    /***
     * This function is called when the fragment is created.
     * It inflates the layout for this fragment and sets the listeners for the date, time and priority fields.
     * It also sets the listener for the addTask button which adds the task to the database.
     *
     * @param inflater: LayoutInflater
     * @param container: ViewGroup?
     * @param savedInstanceState: Bundle?
     * @return View
     *
     * @see OnTaskAddedListener
     * @see TodoDataBaseHandler
     * @see Todo
     * @see DatePickerDialog
     * @see TimePickerDialog
     * @see Calendar
     * @see View
     * @see ViewGroup
     * @see Bundle
     * @see LayoutInflater
     * @see EditText
     * @see Button
     * @see Toast
     * @see BottomSheetDialogFragment
     * @see R.layout.fragment_create_task
     * @see R.id.addTask
     */
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
        val priority = view.findViewById<EditText>(R.id.taskPriority)

        // Set the listeners for the date, time and priority fields
        date.setOnTouchListener { _: View?, motionEvent: MotionEvent ->
            // Get Current Date
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

                // Set the minimum date to the current date
                datePickerDialog!!.datePicker.minDate = System.currentTimeMillis() - 1000
                datePickerDialog!!.show()
            }
            true
        }

        time.setOnTouchListener { _: View?, motionEvent: MotionEvent ->
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
        }

        priority.setOnClickListener {
            val priorityList = arrayOf("High", "Medium", "Low")

            // Create a dialog to select the priority
            val builder = android.app.AlertDialog.Builder(context)
            builder.setTitle("Select Priority")
            // Set the items of the dialog to the priority list
            builder.setItems(priorityList) { _, which ->
                priority.setText(priorityList[which])
            }
            val dialog = builder.create()
            dialog.show()
        }

        addTask.setOnClickListener {
            // Check if all fields are filled
            if (title.text.isNotEmpty() && description.text.isNotEmpty() && date.text.isNotEmpty() && time.text.isNotEmpty() && priority.text.isNotEmpty()) {
                // Get the userId from the arguments
                val userId = arguments?.getInt("userId")
                // Create a new Todo object
                val todo = Todo(
                    title.text.toString(),
                    description.text.toString(),
                    date.text.toString(),
                    time.text.toString(),
                    priority.text.toString(),
                    false,
                    userId!!
                )

                // Insert the task into the database
                val db = TodoDataBaseHandler(requireContext())
                db.insertTodoData(todo)
                listener?.onTaskAdded()

                // Set the alarm
                val alarmManager = requireActivity().getSystemService(ALARM_SERVICE) as AlarmManager
                val alertIntent = Intent(requireContext(), AlertReceiver::class.java)

                // Parse the date and time of the task
                val dateTimeFormat = SimpleDateFormat("dd-M-yyyy H:m", Locale.getDefault())
                val dateTime = dateTimeFormat.parse("${date.text} ${time.text}")

                // Set the alarm for the task's due time
                dateTime?.time?.let {
                    val pendingIntentExact =
                        PendingIntent.getBroadcast(
                            requireContext(),
                            0,
                            alertIntent,
                            PendingIntent.FLAG_IMMUTABLE
                        )
                    alarmManager.set(AlarmManager.RTC_WAKEUP, it, pendingIntentExact)

                    // Set the alarm for 5 minutes before the task's due time
                    val pendingIntentEarly =
                        PendingIntent.getBroadcast(
                            requireContext(),
                            1,
                            alertIntent,
                            PendingIntent.FLAG_IMMUTABLE
                        )
                    alarmManager.set(
                        AlarmManager.RTC_WAKEUP,
                        it - 5 * 60 * 1000,
                        pendingIntentEarly
                    )
                }

                Toast.makeText(context, "Task added", Toast.LENGTH_SHORT).show()
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