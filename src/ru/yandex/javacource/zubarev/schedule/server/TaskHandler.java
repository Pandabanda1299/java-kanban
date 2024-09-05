package ru.yandex.javacource.zubarev.schedule.server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.javacource.zubarev.schedule.manager.TaskManager;

import java.io.IOException;


public class TaskHandler extends BaseHttpHandler implements HttpHandler {
    private  Gson gson;
    private  TaskManager taskManager;
    String response;

    public TaskHandler(TaskManager taskManager, Gson gson) {
        this.taskManager = taskManager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        super.handle(exchange);
        try(exchange) {
            int TaskId = 0;
            String path = exchange.getRequestURI().getPath();
            String[] arrayPath = path.split("/");
            String smethod =  exchange.getRequestMethod();
            if (arrayPath.length > 2) {
                TaskId = Integer.parseInt(arrayPath[3]);
            }
            switch (smethod) {
                case "GET":
                  writeResponse(exchange, taskManager.getTasks().toString(),200);
            }
        }
    }
}