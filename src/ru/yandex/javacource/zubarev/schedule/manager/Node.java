package ru.yandex.javacource.zubarev.schedule.manager;

import ru.yandex.javacource.zubarev.schedule.task.Task;

public class Node<T> {
    Task task;
    Node<Task> prev;
    Node<Task> next;

    public Node(Task task) {
        this.task = task;
        this.next = null;
    }

    public Node(Node<Task> prev, Task task, Node<Task> next) {
        this.prev = prev;
        this.task = task;
        this.next = next;
    }
}
