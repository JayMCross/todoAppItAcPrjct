package com.mashkov.todoister

import android.content.ClipData
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mashkov.todoister.adapter.OnTodoClickListener
import com.mashkov.todoister.adapter.RecyclerViewAdapter
import com.mashkov.todoister.model.SharedViewModel
import com.mashkov.todoister.model.Task
import com.mashkov.todoister.model.TaskViewModel
import com.mashkov.todoister.model.TaskViewModel.Companion.delete

class MainActivity : AppCompatActivity(), OnTodoClickListener {
    private var taskViewModel: TaskViewModel? = null
    private var recyclerView: RecyclerView? = null
    private var recyclerViewAdapter: RecyclerViewAdapter? = null
    private var counter = 0
    var bottomSheetFragment: BottomSheetFragment? = null
    private var sharedViewModel: SharedViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        counter = 0
        bottomSheetFragment = BottomSheetFragment()
        val constraintLayout = findViewById<ConstraintLayout>(R.id.bottomSheet)
        val bottomSheetBehavior = BottomSheetBehavior.from(constraintLayout)
        bottomSheetBehavior.peekHeight = BottomSheetBehavior.STATE_HIDDEN
        recyclerView = findViewById(R.id.recycler_view)
        recyclerView?.setHasFixedSize(true)
        recyclerView?.setLayoutManager(LinearLayoutManager(this))
        taskViewModel = AndroidViewModelFactory(
                this@MainActivity.application)
                .create(TaskViewModel::class.java)
        sharedViewModel = ViewModelProvider(this)
                .get(SharedViewModel::class.java)
        taskViewModel!!.allTasks.observe(this, { tasks: List<Task?>? ->
            recyclerViewAdapter = RecyclerViewAdapter(tasks as List<Task>, this)
            recyclerView?.setAdapter(recyclerViewAdapter)
        })
        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener { view: View? ->
            //Task task = new Task("Task" + counter++, Priority.MEDIUM, Calendar.getInstance().getTime(),
            //        Calendar.getInstance().getTime(), false);

            //taskViewModel.insert(task);
            showBottomSheetDialog()
        }
    }

    private fun showBottomSheetDialog() {
        bottomSheetFragment!!.show(supportFragmentManager, bottomSheetFragment!!.tag)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId
        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)
    }

    override fun onTodoClick(task: Task?) {
        sharedViewModel!!.selectedItem(task)
        sharedViewModel!!.isEdit = true
        Log.d("Click", "onTodoClick: " + task!!.getTask())
        showBottomSheetDialog()
    }

    override fun onTodoRadioButtonClick(task: Task?) {
        Log.d("Click", "onRadioButton: " + task!!.getTask())
        delete(task)
        recyclerViewAdapter!!.notifyDataSetChanged()
    }

    companion object {
        private const val TAG = "ITEM"
    }

}