package ru.yandex.javacource.zubarev.schedule.manager;

import ru.yandex.javacource.zubarev.schedule.task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private final HashMap<Integer, HistoryNode> taskMap = new HashMap<>();
    private HistoryNode head;
    private HistoryNode tail;


        @Override
        public void add(Task task) {

            if (task == null) {
                return;
            }
            if (taskMap.containsKey(task.getId())) {
                removeNode(taskMap.get(task.getId()));
            }
            linkLast(task);
            taskMap.put(task.getId(), tail);
    }

    private void removeNode(HistoryNode node) {
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
        HistoryNode current = head;
        while (current != null) {
            history.add(current.task);
            current = current.next;
        }
        return history;
    }

    @Override
    public void remove(int id) {
        HistoryNode node = taskMap.get(id);
        if (node != null) {
            removeNode(node);
            taskMap.remove(id);
        }
    }

    private void linkLast(Task task) {
        HistoryNode node = new HistoryNode(task);
        if (tail == null) {
            head = node;
            tail = node;
        } else {
            tail.next = node;
            node.prev = tail;
            tail = node;
        }
    }


}


