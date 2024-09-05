package ru.yandex.javacource.zubarev.schedule.server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.javacource.zubarev.schedule.manager.TaskManager;

import java.io.IOException;


public class HistoryHandler extends BaseHttpHandler implements HttpHandler {
    private  Gson gson;
    private  TaskManager taskManager;
    String response;

    public HistoryHandler (TaskManager taskManager, Gson gson) {
        this.taskManager = taskManager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        super.handle(exchange);
    }

}