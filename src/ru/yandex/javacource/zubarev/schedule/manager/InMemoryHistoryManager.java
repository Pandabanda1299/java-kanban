package ru.yandex.javacource.zubarev.schedule.manager;

import ru.yandex.javacource.zubarev.schedule.task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    private final Map<Integer, Node<Task>> History = new HashMap<>();
    private Node<Task> head;
    private Node<Task> tail;


    @Override
    public void add(Task task) {

        if (History.containsKey(task.getId())) {
            removeNode(History.remove(task.getId()));
        }
        linkLast(task);
        History.put(task.getId(), tail);
    }

    private void removeNode(Node node) {
        if (node.prev != null) {
            node.prev.next = node.next;
        } else {
            head = node.next;
        }

        if (node.next != null) {
            node.next.prev = node.prev;
        } else {
            tail = node.prev;
        }
    }


    @Override
    public List<Task> getHistory() {
        List<Task> history = new ArrayList<>();
        Node<Task> current = head;
        while (current != null) {
            history.add(current.task);
            current = current.next;
        }
        return history;
    }

    @Override
    public void remove(int id) {
        final Node node = History.remove(id);
        if (node == null) {
            return;
        }
        removeNode(node);
    }

//        private void linkLast (Task task){
//            Node node = new Node(task);
//            if (tail == null) {
//                head = node;
//                tail = node;
//            } else {
//                tail.next = node;
//                node.prev = tail;
//                tail = node;
//            }
//        }

    private void linkLast(Task task) {
        final Node<Task> node = new Node<Task>(tail, task, null);
        if (head == null) {
            head = node;
        } else {
            tail.next = node;
        }
        tail = node;
    }


}



