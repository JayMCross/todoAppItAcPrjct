package com.mashkov.todoApp.adapter

import com.mashkov.todoApp.model.Task

interface OnTodoClickListener {
    fun onTodoClick(task: Task?)
    fun onTodoRadioButtonClick(task: Task?)
}