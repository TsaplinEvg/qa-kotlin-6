package com.todo.model

data class Task(
    val id: Int = 0,
    val title: String,
    val description: String,
    val isDone: Boolean = false
)
