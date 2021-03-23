package com.leojohan.todo.tasklist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.leojohan.todo.network.Api

class TasksRepository {
    private val tasksWebService = Api.tasksWebService



    // Ces deux variables encapsulent la même donnée:
    // [_taskList] est modifiable mais privée donc inaccessible à l'extérieur de cette classe
    private val _taskList = MutableLiveData<List<Task>>()
    // [taskList] est publique mais non-modifiable:
    // On pourra seulement l'observer (s'y abonner) depuis d'autres classes
    public val taskList: LiveData<List<Task>> = _taskList

    private val webService = Api.tasksWebService
    suspend fun loadTasks(): List<Task>? {
        val response = webService.getTasks()
        return if (response.isSuccessful) response.body() else null
    }

    suspend fun create(task:Task){
        // Call HTTP (opération longue):
        val createResponse = tasksWebService.createTask(task)

        val editableList = _taskList.value.orEmpty().toMutableList()
        editableList.add(createResponse.body()!!)
        _taskList.value = editableList
    }

    suspend fun update(task:Task){
        val updateResponse = tasksWebService.createTask(task)
        val updatedTask = updateResponse.body()!! //
        val editableList = _taskList.value.orEmpty().toMutableList()
        val position = editableList.indexOfFirst { task.id == it.id }
        editableList[position] = updatedTask
        _taskList.value = editableList
    }

    suspend fun delete(task: Task){
        val deleteResponse = tasksWebService.deleteTask(task.id)
        val editableList = _taskList.value.orEmpty().toMutableList()
        editableList.remove(task)
        _taskList.value = editableList
    }
    suspend fun refresh() {
        // Call HTTP (opération longue):
        val tasksResponse = tasksWebService.getTasks()
        // À la ligne suivante, on a reçu la réponse de l'API:
        if (tasksResponse.isSuccessful) {
            val fetchedTasks = tasksResponse.body()
            // on modifie la valeur encapsulée, ce qui va notifier ses Observers et donc déclencher leur callback
            _taskList.value = fetchedTasks
        }
    }
}

