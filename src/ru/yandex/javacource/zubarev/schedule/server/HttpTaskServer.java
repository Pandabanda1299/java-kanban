package ru.yandex.javacource.zubarev.schedule.server;

import com.sun.net.httpserver.HttpServer;
import ru.yandex.javacource.zubarev.schedule.manager.InMemoryTaskManager;
import ru.yandex.javacource.zubarev.schedule.manager.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private TaskManager manager;
    private final HttpServer httpServer;


    public HttpTaskServer(TaskManager manager) throws IOException {
        this.manager = new InMemoryTaskManager();
        httpServer = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        httpServer.createContext("/tasks", new TaskHandler(manager));
        httpServer.createContext("/epics", new EpicHandler(manager));
        httpServer.createContext("/subtasks", new SubtaskHandler(manager));
        httpServer.createContext("/history", new HistoryHandler(manager));
        httpServer.createContext("/prioritized", new PrioritizedTasksHandler(manager));
    }

    public static void main(String[] args) {
        try {
            HttpTaskServer server = new HttpTaskServer(new InMemoryTaskManager());
            server.start();
        } catch (IOException e) {
            System.err.println("Не удалось запустить сервер");
        }
    }

    public void start() throws IOException {
        httpServer.start();
    }

    public void stop() {
        try {
            httpServer.stop(0);
        } catch (Exception e) {
            System.err.println("Не удалось остановить сервер");
        }
    }
}
