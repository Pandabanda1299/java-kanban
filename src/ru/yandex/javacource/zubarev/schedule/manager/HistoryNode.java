package ru.yandex.javacource.zubarev.schedule.manager;


import ru.yandex.javacource.zubarev.schedule.task.Task;

public class HistoryNode {
    Task task;
    HistoryNode prev;
    HistoryNode next;

    HistoryNode(Task task) {
        this.task = task;
        this.next = null;
    }
}
