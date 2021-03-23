package com.leojohan.todo.tasklist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.leojohan.todo.R

class TaskListAdapter(val taskList: MutableList<Task> = mutableListOf()) : RecyclerView.Adapter<TaskListAdapter.TaskViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskListAdapter.TaskViewHolder{
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_task,parent,false)
        return TaskViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = taskList[position]
        holder.bind(task)

    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView = itemView.findViewById<TextView>(R.id.task_title)
        private val deleteButton = itemView.findViewById<ImageButton>(R.id.DeleteButton)
        private val editButton = itemView.findViewById<ImageButton>(R.id.EditButton)
        fun bind(taskTitle: Task) {
            textView.text = taskTitle.toString()
            deleteButton.setOnClickListener{
                onDeleteTask?.invoke(taskTitle)
            }
            editButton.setOnClickListener {
                onEditTask?.invoke(taskTitle)
            }
        }
    }
    var onDeleteTask: ((Task) -> Unit)? = null
    var onEditTask: ((Task) -> Unit)? = null

}
