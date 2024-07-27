package ru.yandex.javacource.zubarev.schedule;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.javacource.zubarev.schedule.task.Epic;
import ru.yandex.javacource.zubarev.schedule.task.ProgressTask;
import ru.yandex.javacource.zubarev.schedule.task.SubTask;
import ru.yandex.javacource.zubarev.schedule.task.Task;
import ru.yandex.javacource.zubarev.schedule.manager.TaskManager;
import ru.yandex.javacource.zubarev.schedule.manager.InMemoryTaskManager;
import ru.yandex.javacource.zubarev.schedule.manager.HistoryManager;
import ru.yandex.javacource.zubarev.schedule.manager.Managers;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestTaskManager {


    InMemoryTaskManager manager = new InMemoryTaskManager();


    @Test
    public void equalityOfTasksWithSameId() {
        Task task = new Task("Задача 1", "Описание 1");
        int id = manager.addTask(task);
        Task savedTask = manager.getTask(id);
        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Созданная задача не совпадает с сохраненной.");

        Epic epic = new Epic("Эпик 1", "Описание эпика 1");
        int epicId = manager.addEpic(epic);
        Epic savedEpic = manager.getEpic(epicId);
        assertNotNull(savedEpic, "Эпик не найден.");
        assertEquals(epic, savedEpic, "Созданный эпик не совпадает с сохраненным.");

        SubTask subtask = new SubTask("Подзадача 1", "Описание подзадачи 1", epicId);
        int subTaskId = manager.addSubTask(subtask);
        SubTask savedSubTask = manager.getSubTask(subTaskId);

        assertNotNull(savedSubTask, "Подзадача не найдена.");
        assertEquals(subtask, savedSubTask, "Созданная подзадача не совпадает с сохраненной.");
    }


    @Test
    public void addingAnEpicToYourself() {

        Epic epic = new Epic("Эпик 1", "Описание эпика 1");
        int epicId = manager.addEpic(epic);
        SubTask subtask = new SubTask("Подзадача 1", "Описание подзадачи 1", epicId);
        subtask.setId(epicId);
        Assertions.assertEquals(-1, manager.addSubTask(subtask));
    }


    @Test
    public void shouldReturnSameInstanceOfTaskManager() {
        TaskManager first = Managers.getDefault();
        TaskManager second = Managers.getDefault();
        assertSame(first, second);
    }

    @Test
    public void shouldReturnSameInstanceOfHistoryManager() {
        HistoryManager first = Managers.getDefaultHistory();
        HistoryManager second = Managers.getDefaultHistory();
        assertSame(first, second);
    }

    @Test
    public void inMemoryTaskManagerTest() {
        Task task = new Task("Задача 1", "Описание 1");
        int id = manager.addTask(task);
        Task savedTask = manager.getTask(id);
        assertEquals(task, savedTask, "Созданная задача не совпадает с сохраненной.");
        assertEquals(id, task.getId(), "Идентификатор задачи не совпадает с ожидаемым.");

        Epic epic = new Epic("Эпик 1", "Описание эпика 1");
        int epicId = manager.addEpic(epic);
        Epic savedEpic = manager.getEpic(epicId);
        assertEquals(epic, savedEpic, "Созданный эпик не совпадает с сохраненным.");
        assertEquals(epicId, epic.getId(), "Идентификатор эпика не совпадает с ожидаемым.");

        SubTask subtask = new SubTask("Подзадача 1", "Описание подзадачи 1", epicId);
        assertNotNull(subtask, "Подзадача не должна быть null.");
        int subtaskId = manager.addSubTask(subtask);
        SubTask savedSubTask = manager.getSubTask(subtaskId);
        assertEquals(subtask, savedSubTask, "Созданная подзадача не совпадает с сохраненной.");
        assertEquals(subtaskId, subtask.getId(), "Идентификатор подзадачи не совпадает с ожидаемым.");
    }

    @Test
    public void givenIdGeneratedId() {

        Task task = new Task("Задача 1", "Описание 1");
        int id = manager.addTask(task);
        int generatedId = task.getId();
        manager.updateTask(new Task("Задача 2", "Описание 2", id));
        assertEquals(generatedId, task.getId());
    }

    @Test
    public void constancyOfTheTask() {

        Task task = new Task("Задача 1", "Описание 1");
        manager.addTask(task);
        assertEquals("Описание 1", task.getName());
        assertEquals("Задача 1", task.getDescription());
        Assertions.assertEquals(ProgressTask.NEW, task.getProgress());
    }

    @Test
    public void savingTaskWhenChanging() {

        Task task1 = new Task("Задача 1", "Описание 1");
        int id = manager.addTask(task1);
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
        SubTask task1 = new SubTask("Подзадача 1", "Описание 1");
        int id = manager.addSubTask(task1);
        manager.getSubTask(id);
        List<Task> savedTasks = manager.getHistory();
        Task savedTask = savedTasks.get(0);
        SubTask task2 = new SubTask("Подзадача 1", "Обновленное Описание 1");
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
        SubTask subtask = new SubTask("Подзадача 1", "Описание подзадачи 1", epicId);
        int subtaskId = manager.addSubTask(subtask);
        manager.deleteSubtask(subtaskId);
        System.out.println(manager.getSubTasks());
        Assertions.assertEquals(0, manager.getTasks(epic).size());

    }

    @Test
    void  epicallyDeleteSubtask() {
        Epic epic = new Epic("Эпик 1", "Описание эпика 1");
        int epicId = manager.addEpic(epic);
        SubTask subtask = new SubTask("Подзадача 1", "Описание подзадачи 1", epicId);
        int subtaskId = manager.addSubTask(subtask);
        manager.deleteSubtask(subtaskId);
        Assertions.assertEquals(0, manager.getSubTasks().size());
    }

    @Test
    void taskChangeDoesNotAffectManager(){
        Task task = new Task("Задача 1", "Описание 1");
        int id = manager.addTask(task);
        task.setDescription("Обновленное Описание 2");
        task.setName("Обновленное Название 2");
        task.setProgress(ProgressTask.IN_PROGRESS);

        Task retrivedTask = manager.getTask(manager.addTask(task));
        Assertions.assertEquals(task, retrivedTask);
        Assertions.assertNotEquals(task.getDescription(), retrivedTask.getDescription());
        Assertions.assertNotEquals(task.getName(), retrivedTask.getName());
        Assertions.assertNotEquals(task.getProgress(), retrivedTask.getProgress());


    }



}
