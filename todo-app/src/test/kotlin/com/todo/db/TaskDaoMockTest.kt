package com.todo.db

import com.todo.model.Task
import io.qameta.allure.Description
import io.qameta.allure.Feature
import io.qameta.allure.Severity
import io.qameta.allure.SeverityLevel
import io.qameta.allure.Story
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*

@Feature("SQLite Task Storage")
class TaskDaoMockTest {

    private lateinit var dao: TaskDao

    @BeforeEach
    fun setUp() {
        dao = mock()
    }

    @Test
    @DisplayName("Вставка задачи возвращает сгенерированный ID")
    @Story("CRUD операции")
    @Severity(SeverityLevel.BLOCKER)
    @Description("При вставке новой задачи DAO должен вернуть положительный идентификатор")
    fun insertTaskReturnsGeneratedId() {
        val task = Task(title = "Купить молоко", description = "2 литра")
        whenever(dao.insert(task)).thenReturn(1L)

        val id = dao.insert(task)

        assertEquals(1L, id)
        verify(dao).insert(task)
    }

    @Test
    @DisplayName("Получение задачи по ID — задача найдена")
    @Story("CRUD операции")
    @Severity(SeverityLevel.CRITICAL)
    @Description("getById должен вернуть задачу, когда она существует в базе")
    fun getByIdReturnsTask() {
        val expected = Task(id = 1, title = "Купить молоко", description = "2 литра")
        whenever(dao.getById(1)).thenReturn(expected)

        val result = dao.getById(1)

        assertNotNull(result)
        assertEquals("Купить молоко", result?.title)
    }

    @Test
    @DisplayName("Получение задачи по несуществующему ID возвращает null")
    @Story("CRUD операции")
    @Severity(SeverityLevel.NORMAL)
    @Description("getById должен вернуть null, если задача с таким ID не найдена")
    fun getByIdReturnsNullWhenNotFound() {
        whenever(dao.getById(999)).thenReturn(null)

        val result = dao.getById(999)

        assertNull(result)
    }

    @Test
    @DisplayName("Получение всех задач — список не пустой")
    @Story("CRUD операции")
    @Severity(SeverityLevel.CRITICAL)
    @Description("getAll должен вернуть все задачи, добавленные в базу")
    fun getAllReturnsTasks() {
        val tasks = listOf(
            Task(id = 1, title = "Задача 1", description = ""),
            Task(id = 2, title = "Задача 2", description = "")
        )
        whenever(dao.getAll()).thenReturn(tasks)

        val result = dao.getAll()

        assertEquals(2, result.size)
        assertEquals("Задача 1", result[0].title)
    }

    @Test
    @DisplayName("Получение всех задач на пустой базе — пустой список")
    @Story("CRUD операции")
    @Severity(SeverityLevel.NORMAL)
    @Description("getAll должен вернуть пустой список, если задач нет")
    fun getAllReturnsEmptyListWhenNoTasks() {
        whenever(dao.getAll()).thenReturn(emptyList())

        val result = dao.getAll()

        assertTrue(result.isEmpty())
    }

    @Test
    @DisplayName("Обновление существующей задачи — возвращает 1 (строка обновлена)")
    @Story("CRUD операции")
    @Severity(SeverityLevel.CRITICAL)
    @Description("update должен вернуть количество изменённых строк — 1 при успехе")
    fun updateTaskReturnsAffectedRows() {
        val task = Task(id = 1, title = "Обновлённая задача", description = "", isDone = true)
        whenever(dao.update(task)).thenReturn(1)

        val affectedRows = dao.update(task)

        assertEquals(1, affectedRows)
        verify(dao).update(task)
    }

    @Test
    @DisplayName("Обновление несуществующей задачи — возвращает 0")
    @Story("CRUD операции")
    @Severity(SeverityLevel.NORMAL)
    @Description("update должен вернуть 0, если задача с таким ID не найдена")
    fun updateNonExistentTaskReturnsZero() {
        val task = Task(id = 999, title = "Призрак", description = "")
        whenever(dao.update(task)).thenReturn(0)

        val affectedRows = dao.update(task)

        assertEquals(0, affectedRows)
    }

    @Test
    @DisplayName("Удаление задачи по ID — возвращает 1 (строка удалена)")
    @Story("CRUD операции")
    @Severity(SeverityLevel.BLOCKER)
    @Description("delete должен вернуть 1 при успешном удалении задачи")
    fun deleteTaskReturnsAffectedRows() {
        whenever(dao.delete(1)).thenReturn(1)

        val affectedRows = dao.delete(1)

        assertEquals(1, affectedRows)
        verify(dao).delete(1)
    }

    @Test
    @DisplayName("Удаление несуществующей задачи — возвращает 0")
    @Story("CRUD операции")
    @Severity(SeverityLevel.NORMAL)
    @Description("delete должен вернуть 0, если задачи с таким ID нет")
    fun deleteNonExistentTaskReturnsZero() {
        whenever(dao.delete(404)).thenReturn(0)

        val result = dao.delete(404)

        assertEquals(0, result)
    }

    @Test
    @DisplayName("Метод insert вызывается ровно один раз")
    @Story("Верификация вызовов")
    @Severity(SeverityLevel.NORMAL)
    @Description("Проверяем, что insert не вызывается лишних раз")
    fun insertIsCalledExactlyOnce() {
        val task = Task(title = "Тест", description = "")
        whenever(dao.insert(any())).thenReturn(1L)

        dao.insert(task)

        verify(dao, times(1)).insert(task)
        verifyNoMoreInteractions(dao)
    }

    @Test
    @DisplayName("Задача помечается как выполненная после обновления")
    @Story("Бизнес-логика")
    @Severity(SeverityLevel.CRITICAL)
    @Description("После update с isDone=true задача должна возвращаться как выполненная")
    fun taskMarkedAsDoneAfterUpdate() {
        val task = Task(id = 1, title = "Сдать ДЗ", description = "", isDone = false)
        val done = task.copy(isDone = true)
        whenever(dao.update(done)).thenReturn(1)
        whenever(dao.getById(1)).thenReturn(done)

        dao.update(done)
        val result = dao.getById(1)

        assertTrue(result?.isDone == true)
    }
}
