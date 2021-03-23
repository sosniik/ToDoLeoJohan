package com.leojohan.todo.tasklist


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.leojohan.todo.R
import com.leojohan.todo.network.Api
import com.leojohan.todo.task.TaskActivity
import kotlinx.coroutines.launch
import java.util.*

class TaskListFragment : Fragment() {
    lateinit var textView : TextView
    private val tasksRepository = TasksRepository()


    private val adapter: TaskListAdapter = TaskListAdapter()
    companion object {
        const val ADD_TASK_REQUEST_CODE = 666
    }

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
            tasksRepository.refresh()
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager= LinearLayoutManager(activity)
        //recyclerView.adapter = TaskListAdapter(taskList)
        super.onViewCreated(view, savedInstanceState)
        textView = view.findViewById<TextView>(R.id.userInfo)

        val addButton = view.findViewById<FloatingActionButton>(R.id.addButton)
        addButton.setOnClickListener{
            val intent = Intent(activity, TaskActivity::class.java)
            startActivityForResult(intent, ADD_TASK_REQUEST_CODE)
        }
        tasksRepository.taskList.observe(viewLifecycleOwner) { list ->
            adapter.taskList.clear()
            adapter.taskList.addAll(list)//modifier la liste et charger les nouvelles valeurs
            adapter.notifyDataSetChanged()
            }
        
        recyclerView.adapter = adapter

        adapter.onEditTask = { task ->

            val intent = Intent(activity, TaskActivity::class.java)
            intent.putExtra(TaskActivity.TASK_KEY, task)
            startActivityForResult(intent, ADD_TASK_REQUEST_CODE)
        }

        adapter.onDeleteTask = { task ->
            lifecycleScope.launch{
                tasksRepository.delete(task)
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_TASK_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val task = data?.getSerializableExtra(TaskActivity.TASK_KEY) as? Task ?: return
            val indexTask = adapter.taskList.indexOfFirst{it.id == task.id}
            if (indexTask < 0) {
                lifecycleScope.launch{
                    tasksRepository.create(task)
                }
            }
            else {
                lifecycleScope.launch{
                    tasksRepository.update(task)
                }
            }
        }
    }
}


