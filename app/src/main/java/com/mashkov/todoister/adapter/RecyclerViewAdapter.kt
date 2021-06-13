package com.mashkov.todoister.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.mashkov.todoister.R
import com.mashkov.todoister.model.Task
import com.mashkov.todoister.util.Utils

class RecyclerViewAdapter(private val taskList: List<Task>, private val todoClickListener: OnTodoClickListener) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.todo_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = taskList[position]
        val formatted = Utils.formatDate(task.getDueDate())
        val colorStateList = ColorStateList(arrayOf(intArrayOf(-android.R.attr.state_enabled), intArrayOf(android.R.attr.state_enabled)), intArrayOf(
                Color.LTGRAY,
                Utils.priorityColor(task)
        ))
        holder.task.text = task.getTask()
        holder.todayChip.text = formatted
        holder.todayChip.setTextColor(Utils.priorityColor(task))
        holder.todayChip.chipIconTint = colorStateList
        holder.radioButton.buttonTintList = colorStateList
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var radioButton: AppCompatRadioButton
        var task: AppCompatTextView
        var todayChip: Chip
        var onTodoClickListener: OnTodoClickListener
        override fun onClick(view: View) {
            val currTask = taskList[adapterPosition]
            val id = view.id
            if (id == R.id.todo_row_layout) {
                onTodoClickListener.onTodoClick(currTask)
            } else if (id == R.id.todo_radio_button) {
                onTodoClickListener.onTodoRadioButtonClick(currTask)
            }
        }

        init {
            radioButton = itemView.findViewById(R.id.todo_radio_button)
            task = itemView.findViewById(R.id.todo_row_todo)
            todayChip = itemView.findViewById(R.id.todo_row_chip)
            onTodoClickListener = todoClickListener
            itemView.setOnClickListener(this)
            radioButton.setOnClickListener(this)
        }
    }
}