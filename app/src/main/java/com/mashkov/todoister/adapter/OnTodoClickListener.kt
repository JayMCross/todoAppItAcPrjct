package com.mashkov.todoister.adapter

import com.mashkov.todoister.model.Task

interface OnTodoClickListener {
    fun onTodoClick(task: Task?)
    fun onTodoRadioButtonClick(task: Task?)
}