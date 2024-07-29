package ru.yandex.javacource.zubarev.schedule;

import ru.yandex.javacource.zubarev.schedule.manager.Managers;
import ru.yandex.javacource.zubarev.schedule.manager.TaskManager;
import ru.yandex.javacource.zubarev.schedule.task.Epic;
import ru.yandex.javacource.zubarev.schedule.task.ProgressTask;
import ru.yandex.javacource.zubarev.schedule.task.SubTask;
import ru.yandex.javacource.zubarev.schedule.task.Task;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();

        // Создание задач

        Task task1 = new Task("Описание задачи 1", "Задача 1");
        Task task2 = new Task("Описание задачи 2", "Задача 2");
        int taskId1 = taskManager.addTask(task1);
        int taskId2 = taskManager.addTask(task2);

        // Создание эпика с двумя подзадачами
        Epic epic1 = new Epic("Описание эпика 1", "Эпик 1");
        int epicId1 = taskManager.addEpic(epic1);
        SubTask subTask1 = new SubTask("Описание подзадачи 1", "Подзадача 1", epicId1);
        SubTask subTask2 = new SubTask("Описание подзадачи 2", "Подзадача 2", epicId1);
        int subtaskId1 = taskManager.addSubTask(subTask1);
        int subtaskId2 = taskManager.addSubTask(subTask2);

        // Создание эпика с одной подзадачей
        Epic epic2 = new Epic("Описание эпика 2", "Эпик 2");
        int epicId2 = taskManager.addEpic(epic2);
        SubTask subTask3 = new SubTask("Описание подзадачи 3", "Подзадача 3", epicId2);
        int subtaskId3 = taskManager.addSubTask(subTask3);

        // Изменить статусы созданных объектов
        task1.setProgress(ProgressTask.DONE);
        subTask1.setProgress(ProgressTask.DONE);
        subTask2.setProgress(ProgressTask.IN_PROGRESS);
        subTask3.setProgress(ProgressTask.NEW);

        // Обновить задачи, эпики и подзадачи
        taskManager.updateTask(task1);
        taskManager.updateEpic(epic1);
        taskManager.updateEpic(epic2);
        taskManager.updateSubtask(subTask1);
        taskManager.updateSubtask(subTask2);
        taskManager.updateSubtask(subTask3);

        // Получить задачу, эпик и подзадачу
        Task retrievedTask = taskManager.getTask(taskId1);
        Epic retrievedEpic = taskManager.getEpic(epicId1);
        SubTask retrievedSubtask = taskManager.getSubTask(subtaskId1);


        // Удалить задачу и эпик
        taskManager.deleteTask(taskId2);
        taskManager.deleteEpic(epicId2);
        taskManager.deleteSubtask(subtaskId2);

        // Вывод информации в консоль
        System.out.println("Задача: " + retrievedTask);
        System.out.println("Эпик: " + retrievedEpic);
        System.out.println("Подзадача: " + retrievedSubtask);
        System.out.println("Подзадачи для эпика: " + subTask1);
    }
}