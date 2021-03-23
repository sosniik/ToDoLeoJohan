package com.leojohan.todo.tasklist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.leojohan.todo.network.Api

class TasksRepository {
    private val tasksWebService = Api.tasksWebService

    private val webService = Api.tasksWebService
    suspend fun loadTasks(): List<Task>? {
        val response = webService.getTasks()
        return if (response.isSuccessful) response.body() else null
    }

    suspend fun create(task:Task): Task? {
        // Call HTTP (opération longue):
        val createResponse = tasksWebService.createTask(task)
        return if(createResponse.isSuccessful) createResponse.body() else null
    }

    suspend fun update(task:Task): Task? {
        val updateResponse = tasksWebService.updateTask(task)
        //val updatedTask = updateResponse.body()!! //
        return if(updateResponse.isSuccessful) updateResponse.body() else null
    }

    suspend fun delete(task: Task): Unit? {
        val deleteResponse = tasksWebService.deleteTask(task.id)
        return if(deleteResponse.isSuccessful) deleteResponse.body() else null
    }
    suspend fun refresh(): List<Task>? {
        // Call HTTP (opération longue):
        val tasksResponse = tasksWebService.getTasks()
        // À la ligne suivante, on a reçu la réponse de l'API:
        return if(tasksResponse.isSuccessful) tasksResponse.body() else null
    }
}

