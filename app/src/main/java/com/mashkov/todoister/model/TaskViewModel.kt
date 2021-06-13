package com.mashkov.todoister.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.mashkov.todoister.data.DoisterRepository

class TaskViewModel(application: Application) : AndroidViewModel(application) {
    val allTasks: LiveData<List<Task>>
    operator fun get(id: Long): LiveData<Task> {
        return repository[id]
    }

    companion object {
        lateinit var repository: DoisterRepository
        @JvmStatic
        fun insert(task: Task?) {
            repository.insert(task)
        }

        @JvmStatic
        fun update(task: Task?) {
            repository.update(task)
        }

        @JvmStatic
        fun delete(task: Task?) {
            repository.delete(task)
        }
    }

    init {
        repository = DoisterRepository(application)
        allTasks = repository.allTasks
    }
}