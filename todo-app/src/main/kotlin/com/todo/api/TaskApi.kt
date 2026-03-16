package com.todo.api

import com.todo.model.Task
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface TaskApi {

    @GET("tasks")
    fun getTasks(): Call<List<Task>>

    @GET("tasks/{id}")
    fun getTask(@Path("id") id: Int): Call<Task>

    @POST("tasks")
    fun createTask(@Body task: Task): Call<Task>

    @PUT("tasks/{id}")
    fun updateTask(@Path("id") id: Int, @Body task: Task): Call<Task>

    @DELETE("tasks/{id}")
    fun deleteTask(@Path("id") id: Int): Call<Void>
}
