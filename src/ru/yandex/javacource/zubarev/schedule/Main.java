package ru.yandex.javacource.zubarev.schedule;

import ru.yandex.javacource.zubarev.schedule.manager.Managers;
import ru.yandex.javacource.zubarev.schedule.manager.TaskManager;
import ru.yandex.javacource.zubarev.schedule.task.Epic;
import ru.yandex.javacource.zubarev.schedule.task.ProgressTask;
import ru.yandex.javacource.zubarev.schedule.task.SubTask;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        TaskManager manager = Managers.getDefault();

        // Создание задач
        Epic epic = new Epic(1, "Epic1", "Описание эпика",ProgressTask.NEW,
                new ArrayList<>(),LocalDateTime.now(),Duration.ofMinutes(10), LocalDateTime.now().plusMinutes(10));
        manager.addEpic(epic);
        SubTask subTask = new SubTask(1, "Описание","Подзадача 1",
                ProgressTask.NEW, epic.getId(),Duration.ofMinutes(30),LocalDateTime.now());
        manager.addSubTask(subTask);

        System.out.println(subTask);
        System.out.println(epic);

    }
}
