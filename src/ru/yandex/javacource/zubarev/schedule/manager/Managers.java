package ru.yandex.javacource.zubarev.schedule.manager;

public class Managers {

    private static TaskManager defaultTaskManager;
    private static HistoryManager defaultHistoryManager;

    public static TaskManager getDefault() {
        if (defaultTaskManager == null) {
            defaultTaskManager = new InMemoryTaskManager();
        }
        return defaultTaskManager;
    }


    public static HistoryManager getDefaultHistory() {
        if (defaultHistoryManager == null) {
            defaultHistoryManager = new InMemoryHistoryManager();
        }
        return defaultHistoryManager;
    }


}


