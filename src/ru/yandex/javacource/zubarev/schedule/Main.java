package ru.yandex.javacource.zubarev.schedule;

import ru.yandex.javacource.zubarev.schedule.manager.Managers;
import ru.yandex.javacource.zubarev.schedule.manager.TaskManager;
import ru.yandex.javacource.zubarev.schedule.task.Epic;
import ru.yandex.javacource.zubarev.schedule.task.SubTask;
import ru.yandex.javacource.zubarev.schedule.task.Task;
import ru.yandex.javacource.zubarev.schedule.task.ProgressTask;

import java.time.Duration;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();

        // Создание задач
        LocalDateTime start = LocalDateTime.now();
        Duration duration = Duration.ofHours(1);

        Task task1 = new Task(0, "Задача 1", "Описание задачи 1", ProgressTask.NEW, start, duration);
        Task task2 = new Task(0, "Задача 2", "Описание задачи 2", ProgressTask.NEW, start.plusHours(2), duration);
        int taskId1 = taskManager.addTask(task1);
        int taskId2 = taskManager.addTask(task2);

        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1");
        int epicId1 = taskManager.addEpic(epic1);

        SubTask subTask1 = new SubTask(0, "Подзадача 1", "Описание подзадачи 1", ProgressTask.NEW, epicId1, duration, start.plusHours(4));
        SubTask subTask2 = new SubTask(0, "Подзадача 2", "Описание подзадачи 2", ProgressTask.NEW, epicId1, duration, start.plusHours(6));
        int subtaskId1 = taskManager.addSubTask(subTask1);
        int subtaskId2 = taskManager.addSubTask(subTask2);


        // Создание эпика с одной подзадачей
        Epic epic2 = new Epic("Эпик 2", "Описание эпика 2");
        int epicId2 = taskManager.addEpic(epic2);
        SubTask subTask3 = new SubTask(0, "Подзадача 3", "Описание подзадачи 3", ProgressTask.NEW, epicId2, duration, start.plusHours(8));
        int subtaskId3 = taskManager.addSubTask(subTask3);

        // Изменение статусов и обновление задач
        task1.setProgress(ProgressTask.DONE);
        subTask1.setProgress(ProgressTask.DONE);
        subTask2.setProgress(ProgressTask.IN_PROGRESS);

        taskManager.updateTask(task1);
        taskManager.updateSubtask(subTask1);
        taskManager.updateSubtask(subTask2);

        // Получение и вывод информации
        System.out.println("Задача: " + taskManager.getTask(taskId1));
        System.out.println("Эпик: " + taskManager.getEpic(epicId1));
        System.out.println("Подзадача: " + taskManager.getSubTask(subtaskId1));

        // Удаление задач
        taskManager.deleteTask(taskId2);
        taskManager.deleteEpic(epicId2);
        taskManager.deleteSubtask(subtaskId2);

        // Вывод истории просмотров
        System.out.println("История просмотров: " + taskManager.getHistory());
    }
}
