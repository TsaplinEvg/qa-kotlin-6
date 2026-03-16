package com.todo.db

import com.todo.model.Task
import java.sql.Connection
import java.sql.DriverManager

class DatabaseHelper(private val connection: Connection) : TaskDao {

    init {
        connection.createStatement().executeUpdate(
            """
            CREATE TABLE IF NOT EXISTS tasks (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                title TEXT NOT NULL,
                description TEXT,
                is_done INTEGER NOT NULL DEFAULT 0
            )
            """.trimIndent()
        )
    }

    override fun insert(task: Task): Long {
        val stmt = connection.prepareStatement(
            "INSERT INTO tasks (title, description, is_done) VALUES (?, ?, ?)"
        )
        stmt.setString(1, task.title)
        stmt.setString(2, task.description)
        stmt.setInt(3, if (task.isDone) 1 else 0)
        stmt.executeUpdate()

        val keys = stmt.generatedKeys
        return if (keys.next()) keys.getLong(1) else -1L
    }

    override fun getById(id: Int): Task? {
        val stmt = connection.prepareStatement("SELECT * FROM tasks WHERE id = ?")
        stmt.setInt(1, id)
        val rs = stmt.executeQuery()
        return if (rs.next()) {
            Task(
                id = rs.getInt("id"),
                title = rs.getString("title"),
                description = rs.getString("description"),
                isDone = rs.getInt("is_done") == 1
            )
        } else null
    }

    override fun getAll(): List<Task> {
        val rs = connection.createStatement().executeQuery("SELECT * FROM tasks")
        val tasks = mutableListOf<Task>()
        while (rs.next()) {
            tasks.add(
                Task(
                    id = rs.getInt("id"),
                    title = rs.getString("title"),
                    description = rs.getString("description"),
                    isDone = rs.getInt("is_done") == 1
                )
            )
        }
        return tasks
    }

    override fun update(task: Task): Int {
        val stmt = connection.prepareStatement(
            "UPDATE tasks SET title = ?, description = ?, is_done = ? WHERE id = ?"
        )
        stmt.setString(1, task.title)
        stmt.setString(2, task.description)
        stmt.setInt(3, if (task.isDone) 1 else 0)
        stmt.setInt(4, task.id)
        return stmt.executeUpdate()
    }

    override fun delete(id: Int): Int {
        val stmt = connection.prepareStatement("DELETE FROM tasks WHERE id = ?")
        stmt.setInt(1, id)
        return stmt.executeUpdate()
    }

    companion object {
        fun inMemory(): DatabaseHelper {
            val conn = DriverManager.getConnection("jdbc:sqlite::memory:")
            return DatabaseHelper(conn)
        }
    }
}
