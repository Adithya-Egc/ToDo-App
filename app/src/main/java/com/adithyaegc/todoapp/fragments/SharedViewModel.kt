package com.adithyaegc.todoapp.fragments

import android.app.Application
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.adithyaegc.todoapp.R
import com.adithyaegc.todoapp.data.models.Priority
import com.adithyaegc.todoapp.data.models.ToDo

class SharedViewModel(application: Application) : AndroidViewModel(application) {

     val dataVisibility: MutableLiveData<Boolean> = MutableLiveData(false)

    fun checkDatabaseEmpty(todo: List<ToDo>) {
        dataVisibility.value = todo.isEmpty()
    }

    val listener: AdapterView.OnItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(
            adapterView: AdapterView<*>?,
            view: View?,
            position: Int,
            id: Long
        ) {
            when (position) {
                0 -> ((adapterView?.getChildAt(0) as TextView).setTextColor(
                    ContextCompat.getColor(
                        application,
                        R.color.red
                    )
                ))
                1 -> ((adapterView?.getChildAt(0) as TextView).setTextColor(
                    ContextCompat.getColor(
                        application,
                        R.color.yellow
                    )
                ))
                2 -> ((adapterView?.getChildAt(0) as TextView).setTextColor(
                    ContextCompat.getColor(
                        application,
                        R.color.green
                    )
                ))
            }

        }

        override fun onNothingSelected(p0: AdapterView<*>?) {
        }
    }


    fun checkValidation(title: String, description: String): Boolean {
        return (title.isNotEmpty() && description.isNotEmpty())
    }


    fun parsePriority(priority: String): Priority {
        return when (priority) {
            "High Priority" -> {
                Priority.HIGH
            }
            "Medium Priority" -> {
                Priority.MEDIUM
            }
            "Low Priority" -> {
                Priority.LOW
            }
            else -> {
                Priority.LOW
            }
        }
    }

    fun parsePriorityToInt(priority: Priority): Int {
        return when (priority) {
            Priority.HIGH -> 0
            Priority.MEDIUM -> 1
            Priority.LOW -> 2
        }
    }

}