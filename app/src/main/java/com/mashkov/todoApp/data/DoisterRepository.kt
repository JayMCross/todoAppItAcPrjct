package com.mashkov.todoApp.data

import android.app.Application
import androidx.lifecycle.LiveData
import com.mashkov.todoApp.model.Task
import com.mashkov.todoApp.util.TaskRoomDatabase

class DoisterRepository(application: Application?) {
    private val taskDao: TaskDao
    val allTasks: LiveData<List<Task>>
    fun insert(task: Task?) {
        TaskRoomDatabase.databaseWriterExecutor.execute { taskDao.insertTask(task) }
    }

    operator fun get(id: Long): LiveData<Task> {
        return taskDao[id]
    }

    fun update(task: Task?) {
        TaskRoomDatabase.databaseWriterExecutor.execute { taskDao.update(task) }
    }

    fun delete(task: Task?) {
        TaskRoomDatabase.databaseWriterExecutor.execute { taskDao.delete(task) }
    }

    init {
        val database = TaskRoomDatabase.getDatabase(application)
        taskDao = database.taskDao() // don't need 'this.'
        allTasks = taskDao.tasks
    }
}