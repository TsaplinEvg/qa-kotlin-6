package com.todo.api

import com.todo.model.Task
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TaskApiService(baseUrl: String, client: OkHttpClient = OkHttpClient()) {

    private val api: TaskApi = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(TaskApi::class.java)

    fun getTasks(): List<Task> {
        return api.getTasks().execute().body() ?: emptyList()
    }

    fun getTask(id: Int): Task? {
        val response = api.getTask(id).execute()
        return if (response.isSuccessful) response.body() else null
    }

    fun createTask(task: Task): Task {
        return api.createTask(task).execute().body()!!
    }

    fun updateTask(task: Task): Task {
        return api.updateTask(task.id, task).execute().body()!!
    }

    fun deleteTask(id: Int): Boolean {
        return api.deleteTask(id).execute().isSuccessful
    }
}
