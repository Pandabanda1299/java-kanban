package ru.yandex.javacource.zubarev.schedule;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.javacource.zubarev.schedule.manager.InMemoryTaskManager;
import ru.yandex.javacource.zubarev.schedule.manager.TaskManager;
import ru.yandex.javacource.zubarev.schedule.task.Epic;
import ru.yandex.javacource.zubarev.schedule.task.ProgressTask;
import ru.yandex.javacource.zubarev.schedule.task.SubTask;
import ru.yandex.javacource.zubarev.schedule.task.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public  class TestTaskManager  {

     protected TaskManager manager = new InMemoryTaskManager();

    @Test
    public void equalityOfTasksWithSameId() {
        Task task = new Task("Задача 1", "Описание 1", ProgressTask.NEW);
        int id = manager.addTask(task);
        Task savedTask = manager.getTask(id);
        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Созданная задача не совпадает с сохраненной.");

        Epic epic = new Epic("Эпик 1", "Описание эпика 1");
        int epicId = manager.addEpic(epic);
        Epic savedEpic = manager.getEpic(epicId);
        assertNotNull(savedEpic, "Эпик не найден.");
        assertEquals(epic, savedEpic, "Созданный эпик не совпадает с сохраненным.");

        SubTask subtask = new SubTask(0, "Подзадача 1", "Описание подзадачи 1", ProgressTask.NEW, epicId);
        int subTaskId = manager.addSubTask(subtask);
        SubTask savedSubTask = manager.getSubTask(subTaskId);

        assertNotNull(savedSubTask, "Подзадача не найдена.");
        assertEquals(subtask, savedSubTask, "Созданная подзадача не совпадает с сохраненной.");
    }


    @Test
    public void inMemoryTaskManagerTest() {
        Task task = new Task(1, "Задача 1", "Описание 1", ProgressTask.NEW, LocalDateTime.now(), Duration.ofMinutes(30));
        int id = manager.addTask(task);
        Task savedTask = manager.getTask(id);
        assertEquals(task, savedTask, "Созданная задача не совпадает с сохраненной.");
        assertEquals(id, task.getId(), "Идентификатор задачи не совпадает с ожидаемым.");

        Epic epic = new Epic(1, "Эпик 1", "Описание 1", ProgressTask.NEW, new ArrayList<>(), LocalDateTime.now().plusHours(2), Duration.ofMinutes(50), LocalDateTime.now().plusHours(1).plusMinutes(30));
        int epicId = manager.addEpic(epic);
        Epic savedEpic = manager.getEpic(epicId);
        assertEquals(epic, savedEpic, "Созданный эпик не совпадает с сохраненным.");
        assertEquals(epicId, epic.getId(), "Идентификатор эпика не совпадает с ожидаемым.");

        SubTask subtask = new SubTask(1, "Подзадача 1", "Описание подзадачи 1", ProgressTask.NEW, epicId, Duration.ofMinutes(30), LocalDateTime.now().plusMinutes(39));
        assertNotNull(subtask, "Подзадача не должна быть null.");
        int subtaskId = manager.addSubTask(subtask);
        SubTask savedSubTask = manager.getSubTask(subtaskId);
        assertEquals(subtask, savedSubTask, "Созданная подзадача не совпадает с сохраненной.");
        assertEquals(subtaskId, subtask.getId(), "Идентификатор подзадачи не совпадает с ожидаемым.");
    }

    @Test
    public void givenIdGeneratedId() {

        Task task = new Task(1, "Задача 1", "Описание 1", ProgressTask.NEW, LocalDateTime.now(), Duration.ofMinutes(30));
        int id = manager.addTask(task);
        int generatedId = task.getId();
        manager.updateTask(new Task(1,"Задача 2", "Описание 2", ProgressTask.NEW, LocalDateTime.now(), Duration.ofMinutes(35)));
        assertEquals(generatedId, task.getId());
    }


    @Test
    public void savingTaskWhenChanging() {

        Task task = new Task(1, "Задача 1", "Описание 1", ProgressTask.NEW, LocalDateTime.now(), Duration.ofMinutes(30));
        int id = manager.addTask(task);
        manager.getTask(id);
        List<Task> tasks = manager.getHistory();
        Task savedTask = tasks.get(0);
        Task task2 = new Task("Задача 1", "Обновленное Описание 1", ProgressTask.IN_PROGRESS);
        manager.updateTask(task2);
        List<Task> updatedTasks = manager.getHistory();
        Task updatedTask = updatedTasks.get(0);
        Assertions.assertEquals(savedTask, updatedTask);
    }

    @Test
    public void savingEpicWhenChanging() {
        Epic task = new Epic(" Эпик 1 ", " Описание 1 ");
        int id = manager.addEpic(task);
        manager.getEpic(id);
        List<Task> savedTasks = manager.getHistory();
        Task savedTask = savedTasks.get(0);
        manager.updateEpic(task);
        Epic task1 = new Epic(" Эпик 1", "Обновленное Описание 1");
        manager.updateEpic(task1);
        List<Task> updatedTasks = manager.getHistory();
        Task updatedTask = updatedTasks.get(0);
        Assertions.assertEquals(savedTask, updatedTask);
    }

    @Test
    public void savingSubTaskWhenChanging() {
        Epic task1488 = new Epic("Эпик 1", "Описание 1");
        manager.addEpic(task1488);
        SubTask task1 = new SubTask(1, "Подзадача 1", "Описание 1", ProgressTask.NEW, task1488.getId(), Duration.ofMinutes(30), LocalDateTime.now());
        int id = manager.addSubTask(task1);
        manager.getSubTask(id);
        List<Task> savedTasks = manager.getHistory();
        Task savedTask = savedTasks.get(0);
        SubTask task2 = new SubTask(id, "Подзадача 1", "Обновленное Описание 1", ProgressTask.NEW, task1488.getId(), Duration.ofMinutes(30), LocalDateTime.now());
        manager.updateSubtask(task2);
        List<Task> updatedTasks = manager.getHistory();
        Task updatedTask = updatedTasks.get(0);
        assertEquals(savedTask, updatedTask);

    }


    @Test
    public void shouldReturnEpic() {
        Epic epic = new Epic("Эпик 1", "Описание эпика 1");
        int epicId = manager.addEpic(epic);
        Epic savedEpic = manager.getEpic(epicId);
        assertEquals(epic, savedEpic, "Созданный эпик не совпадает с сохраненным.");
    }


    @Test
    void shouldRemoveSubtask() {
        Epic epic = new Epic("Эпик 1", "Описание эпика 1");
        int epicId = manager.addEpic(epic);
        SubTask subtask = new SubTask(1, "Подзадача 1", "Описание подзадачи 1", ProgressTask.NEW, epicId, Duration.ofMinutes(10), LocalDateTime.now());
        int subtaskId = manager.addSubTask(subtask);
        manager.deleteSubtask(subtaskId);
        System.out.println(manager.getSubTasks());
        Assertions.assertEquals(0, manager.getTasks(epicId ).size());

    }

    @Test
    void epicallyDeleteSubtask() {
        Epic epic = new Epic("Эпик 1", "Описание эпика 1");
        int epicId = manager.addEpic(epic);
        SubTask subtask = new SubTask(1, "Подзадача 1", "Описание подзадачи 1",ProgressTask.NEW, epicId);
        int subtaskId = manager.addSubTask(subtask);
        manager.deleteSubtask(subtaskId);
        Assertions.assertEquals(0, manager.getSubTasks().size());
    }


    @Test
    void taskChangeDoesNotAffectManager() {
        Epic epic = new Epic("1", "2");
        int epicId = manager.addEpic(epic);
        SubTask subtask = new SubTask(1, "Подзадача 1", "Описание подзадачи 1", ProgressTask.NEW, epicId, Duration.ofMinutes(10), LocalDateTime.now());
        Task task = new Task(1, "Задача 1", "Описание 1", ProgressTask.NEW, LocalDateTime.now(), Duration.ofMinutes(30));
        int subtaskId = manager.addSubTask(subtask);
        subtask.setId(293);

        Assertions.assertNotEquals(subtask, manager.getSubTask(subtaskId));

    }

}
