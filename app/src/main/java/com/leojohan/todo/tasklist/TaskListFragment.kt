package com.leojohan.todo.tasklist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.leojohan.todo.R
import com.leojohan.todo.network.Api
import kotlinx.coroutines.launch
import java.util.*

class TaskListFragment : Fragment() {
    lateinit var textView : TextView;

    private val taskList = mutableListOf(
            Task(id = "id_1", title = "Task 1", description = "description 1"),
            Task(id = "id_2", title = "Task 2"),
            Task(id = "id_3", title = "Task 3")
    )
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        return inflater.inflate(R.layout.fragment_task_list, container, false)
    }

    override fun onResume(){
        super.onResume()
        lifecycleScope.launch {
            val userInfo = Api.userService.getInfo().body()!!
            val text = "${userInfo.firstName} ${userInfo.lastName}"

            textView.text = text
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager= LinearLayoutManager(activity)
        //recyclerView.adapter = TaskListAdapter(taskList)
        super.onViewCreated(view, savedInstanceState)
        textView = view.findViewById<TextView>(R.id.userInfo)

        val adapter = TaskListAdapter(taskList)
        val addButton = view.findViewById<FloatingActionButton>(R.id.addButton)
        addButton.setOnClickListener{
            val taskAdd = Task(id = UUID.randomUUID().toString(), title = "Task ${taskList.size + 1}")
            taskList.add(taskAdd)
            adapter.notifyDataSetChanged()
        }

        recyclerView.adapter = adapter

        adapter.onDeleteTask = { task ->
            taskList.remove(task)
            adapter.notifyDataSetChanged()
        }
    }
}


