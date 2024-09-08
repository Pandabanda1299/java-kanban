package ru.yandex.javacource.zubarev.schedule.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import ru.yandex.javacource.zubarev.schedule.manager.TaskManager;
import ru.yandex.javacource.zubarev.schedule.task.Task;

import javax.xml.datatype.Duration;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class HistoryHandler extends BaseHttpHandler {
    private final TaskManager taskManager;
    private final Gson gson;

    public HistoryHandler(TaskManager taskManager, Gson gson) {
        this.taskManager = taskManager;
        this.gson = new GsonBuilder()
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter())
                .create();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String response;

        if ("GET".equals(method)) {
            List<Task> history = taskManager.getHistory();
            response = gson.toJson(history);
            writeResponse(exchange, response, 200);
        } else {
            writeResponse(exchange, "Метод не поддерживается", 405);
        }
    }
}
