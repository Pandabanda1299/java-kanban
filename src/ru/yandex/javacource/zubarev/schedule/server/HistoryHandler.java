package ru.yandex.javacource.zubarev.schedule.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.javacource.zubarev.schedule.manager.TaskManager;
import ru.yandex.javacource.zubarev.schedule.task.Task;

import java.io.IOException;
import java.util.List;

public class HistoryHandler extends BaseHttpHandler implements HttpHandler {


    public HistoryHandler(TaskManager taskManager) {
        super(taskManager);
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
