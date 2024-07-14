package ru.yandex.javacource.zubarev.schedule.manager;

import ru.yandex.javacource.zubarev.schedule.task.Task;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private static final int MAX_HISTORY_SIZE = 10;
    private final LinkedList<Task> history = new LinkedList<>();

    @Override
    public void add(Task task) {
        if (task != null) {
            history.addLast(task);
            if (history.size() > MAX_HISTORY_SIZE) {
                history.removeFirst();
            }
        }
    }

    @Override
    public List<Task> getHistory() {
        return new LinkedList<>(history);
    }
}


