package ru.yandex.javacource.zubarev.schedule.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.javacource.zubarev.schedule.manager.TaskManager;
import ru.yandex.javacource.zubarev.schedule.task.Task;

import java.io.IOException;
import java.io.InputStream;


public class TaskHandler extends BaseHttpHandler implements HttpHandler {
    String response;

    public TaskHandler(TaskManager taskManager) {
        super(taskManager);
    }


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = String.valueOf(exchange.getRequestURI());

        System.out.println("Обрабатывается запрос " + path + " с методом " + method);
        switch (method) {
            case "GET":
                getTask(exchange);
                break;
            case "POST":
                addTask(exchange);
                break;
            case "DELETE":
                deleteTask(exchange);
                break;
            default:
                writeResponse(exchange, "Такой операции не существует", 404);
        }
    }


    private void getTask(HttpExchange exchange) throws IOException {
        if (exchange.getRequestURI().getQuery() == null) {
            response = gson.toJson(taskManager.getTasks());
            writeResponse(exchange, response, 200);
            return;
        }

        if (getTaskId(exchange).isEmpty()) {
            writeResponse(exchange, "Некорректный идентификатор " + getTaskId(exchange), 400);
            return;
        }
        int id = getTaskId(exchange).get();

        response = gson.toJson(id);
        writeResponse(exchange, response, 200);
    }

    private void addTask(HttpExchange exchange) {
        try {
            InputStream json = exchange.getRequestBody();
            String jsonTask = new String(json.readAllBytes(), DEFAULT_CHARSET);
            Task task = gson.fromJson(jsonTask, Task.class);
            if (task == null) {
                writeResponse(exchange, "Задача не должна быть пустой!", 400);
                return;
            }
            Task id = taskManager.getTask(task.getId());
            if (id == null) {
                taskManager.addTask(task);
                writeResponse(exchange, "Задача добавлена!", 201);
                return;
            }
            taskManager.updateTask(task);
            writeResponse(exchange, "Задача обновлена", 200);

        } catch (IOException e) {
            System.out.println("Ошибка добавления");
        }
    }

    private void deleteTask(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String[] ids = path.split("/");
        int id = 0;

        if (ids.length > 2) {
            id = Integer.parseInt(ids[2]);
        }

        if (id == 0) {
            writeResponse(exchange, "Не указан id задачи ", 404);
            return;
        }

        if (taskManager.getTasks(id) == null) {
            writeResponse(exchange, "Задач с таким id " + id + " не найдено!", 404);
            return;
        }

        taskManager.deleteTask(id);
        writeResponse(exchange, "Задача удалена!", 200);
    }

}