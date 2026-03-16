package com.todo.db

import com.todo.model.Task

interface TaskDao {
    fun insert(task: Task): Long
    fun getById(id: Int): Task?
    fun getAll(): List<Task>
    fun update(task: Task): Int
    fun delete(id: Int): Int
}
