package com.leojohan.todo.tasklist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class TaskListViewModel : ViewModel() {
    private val repository = TasksRepository()
    private val _taskList = MutableLiveData<List<Task>>()
    public val taskList: LiveData<List<Task>> = _taskList

    fun loadTasks() {
        viewModelScope.launch{
            val fetchedTasks = repository.loadTasks()
            if(fetchedTasks != null){
                _taskList.value = fetchedTasks!!
            }
        }
    }
    fun deleteTask(task: Task) {
        viewModelScope.launch{
            val editableList = _taskList.value.orEmpty().toMutableList()
            editableList.remove(task)
            _taskList.value = editableList
        }
    }
    fun addTask(task: Task) {
        viewModelScope.launch{
            val createdTask = repository.create(task)
            val editableList = _taskList.value.orEmpty().toMutableList()
            editableList.add(editableList.size,task)
            _taskList.value = editableList
        }
    }
    fun editTask(task: Task) {
        viewModelScope.launch{
            val editedTask = repository.update(task)
            val updatedTask = editedTask!!
            val editableTask = _taskList.value.orEmpty().toMutableList()
            val position = editableTask.indexOfFirst { task.id == it.id}
            editableTask[position] = task
            _taskList.value = editableTask
        }
    }
}

