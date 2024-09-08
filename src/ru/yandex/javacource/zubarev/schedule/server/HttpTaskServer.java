package ru.yandex.javacource.zubarev.schedule.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import ru.yandex.javacource.zubarev.schedule.manager.Managers;
import ru.yandex.javacource.zubarev.schedule.manager.TaskManager;

import javax.xml.datatype.Duration;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.LocalDateTime;

public class HttpTaskServer  {
    private static final int PORT = 8080;
    private static final TaskManager TASK_MANAGER = Managers.getDefault();
    private final HttpServer httpServer;
    Gson gson = new GsonBuilder()
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter())
            .create();


    public HttpTaskServer() throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        httpServer.createContext("/tasks", new TaskHandler(TASK_MANAGER,gson));
        httpServer.createContext("/epics", new EpicHandler(TASK_MANAGER,gson));
        httpServer.createContext("/subtasks", new SubtaskHandler(TASK_MANAGER,gson));
        httpServer.createContext("/history", new HistoryHandler(TASK_MANAGER,gson));
        httpServer.createContext("/prioritized", new PrioritizedTasksHandler(TASK_MANAGER,gson));
    }

    public static void main(String[] args) {
        try {
            HttpTaskServer server = new HttpTaskServer();
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