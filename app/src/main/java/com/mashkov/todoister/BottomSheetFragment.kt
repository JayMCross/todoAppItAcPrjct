package com.mashkov.todoister

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.Group
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import com.mashkov.todoister.model.Priority
import com.mashkov.todoister.model.SharedViewModel
import com.mashkov.todoister.model.Task
import com.mashkov.todoister.model.TaskViewModel.Companion.insert
import com.mashkov.todoister.model.TaskViewModel.Companion.update
import com.mashkov.todoister.util.Utils.hideSoftKeyboard
import java.util.*

class BottomSheetFragment : BottomSheetDialogFragment(), View.OnClickListener {
    private var enterTodo: EditText? = null
    private var calendarButton: ImageButton? = null
    private var priorityButton: ImageButton? = null
    private var priorityRadioGroup: RadioGroup? = null
    private var selectedRadioButton: RadioButton? = null
    private var selectedButtonId = 0
    private var saveButton: ImageButton? = null
    private var calendarView: CalendarView? = null
    private var calendarGroup: Group? = null
    private var dueDate: Date? = null
    var calendar = Calendar.getInstance()
    private var sharedViewModel: SharedViewModel? = null
    private var isEdit = false
    private var priority: Priority? = null
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.bottom_sheet, container, false)
        calendarGroup = view.findViewById(R.id.calendar_group)
        calendarView = view.findViewById(R.id.calendar_view)
        calendarButton = view.findViewById(R.id.today_calendar_button)
        enterTodo = view.findViewById(R.id.enter_todo_et)
        saveButton = view.findViewById(R.id.save_todo_button)
        priorityButton = view.findViewById(R.id.priority_todo_button)
        priorityRadioGroup = view.findViewById(R.id.radioGroup_priority)
        val todayChip: Chip = view.findViewById(R.id.today_chip)
        todayChip.setOnClickListener(this)
        val tomorrowChip: Chip = view.findViewById(R.id.tomorrow_chip)
        tomorrowChip.setOnClickListener(this)
        val nextWeekChip: Chip = view.findViewById(R.id.next_week_chip)
        nextWeekChip.setOnClickListener(this)
        return view
    }

    override fun onResume() {
        super.onResume()
        if (sharedViewModel!!.selectedItem.value != null) {
            isEdit = sharedViewModel!!.isEdit
            val task = sharedViewModel!!.selectedItem.value
            enterTodo!!.setText(task!!.getTask())
            Log.d("MY", "onViewCreated: " + task.getTask())
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel = ViewModelProvider(requireActivity())
                .get(SharedViewModel::class.java)
        calendarButton!!.setOnClickListener { view2: View? ->
            calendarGroup!!.visibility = if (calendarGroup!!.visibility == View.GONE) View.VISIBLE else View.GONE
            hideSoftKeyboard(view2!!)
        }
        calendarView!!.setOnDateChangeListener { calendarView: CalendarView?, year: Int, month: Int, dayOfMonth: Int ->
            calendar.clear()
            calendar[year, month] = dayOfMonth
            dueDate = calendar.time
        }
        priorityButton!!.setOnClickListener { view13: View? ->
            hideSoftKeyboard(view13!!)
            priorityRadioGroup!!.visibility = if (priorityRadioGroup!!.visibility == View.GONE) View.VISIBLE else View.GONE
            priorityRadioGroup!!.setOnCheckedChangeListener { group: RadioGroup?, checkedId: Int ->
                if (priorityRadioGroup!!.visibility == View.VISIBLE) {
                    selectedButtonId = checkedId
                    selectedRadioButton = view.findViewById(selectedButtonId)
                    priority = if (selectedRadioButton?.getId() == R.id.radioButton_high) {
                        Priority.HIGH
                    } else if (selectedRadioButton?.getId() == R.id.radioButton_med) {
                        Priority.MEDIUM
                    } else if (selectedRadioButton?.getId() == R.id.radioButton_low) {
                        Priority.LOW
                    } else {
                        Priority.LOW
                    }
                } else {
                    priority = Priority.LOW
                }
            }
        }
        saveButton!!.setOnClickListener { view1: View? ->
            val task = enterTodo!!.text.toString().trim { it <= ' ' }
            if (!TextUtils.isEmpty(task) && dueDate != null && priority != null) {
                val myTask = Task(task, priority,
                        dueDate, Calendar.getInstance().time,
                        false)
                if (isEdit) {
                    val updateTask = sharedViewModel!!.selectedItem.value
                    updateTask!!.setTask(task)
                    updateTask.setDateCreated(Calendar.getInstance().time)
                    updateTask.setPriority(priority)
                    updateTask.setDueDate(dueDate)
                    update(updateTask)
                    sharedViewModel!!.isEdit = false
                } else {
                    insert(myTask)
                }
                enterTodo!!.setText("")
                if (this.isVisible) {
                    dismiss()
                }
            } else {
                Snackbar.make(saveButton!!, R.string.empty_field, Snackbar.LENGTH_LONG)
                        .show()
            }
        }
    }

    override fun onClick(view: View) {
        val id = view.id
        if (id == R.id.today_chip) {
            calendar.add(Calendar.DAY_OF_YEAR, 0)
            dueDate = calendar.time
            Log.d("TIME", "onClick" + dueDate.toString())
        } else if (id == R.id.tomorrow_chip) {
            calendar.add(Calendar.DAY_OF_YEAR, 1)
            dueDate = calendar.time
            Log.d("TIME", "onClick" + dueDate.toString())
        } else if (id == R.id.next_week_chip) {
            calendar.add(Calendar.DAY_OF_YEAR, 7)
            dueDate = calendar.time
            Log.d("TIME", "onClick" + dueDate.toString())
        }
    }
}