package ru.yandex.javacource.zubarev.schedule.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.javacource.zubarev.schedule.manager.TaskManager;
import ru.yandex.javacource.zubarev.schedule.task.Task;

import javax.xml.datatype.Duration;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;

import static java.util.Objects.isNull;


public class TaskHandler extends BaseHttpHandler implements HttpHandler {
    private  Gson gson;
    private  TaskManager taskManager;
    String response;

    public TaskHandler(TaskManager taskManager, Gson gson) {
        this.taskManager = taskManager;
        this.gson = new GsonBuilder()
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter())
                .create();
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
        Task Id = taskManager.getTasks().get(id);
        if (isNull(id)) {
            writeResponse(exchange, "Задач с id " + id + " не найдено!", 404);
            return;
        }
        response = gson.toJson(id);
        writeResponse(exchange, response, 200);
    }

    private void addTask(HttpExchange exchange) throws IOException {
        try {
            InputStream json = exchange.getRequestBody();
            String jsonTask = new String(json.readAllBytes(), DEFAULT_CHARSET);
            Task task = gson.fromJson(jsonTask, Task.class);
            if (task == null) {
                writeResponse(exchange, "Задача не должна быть пустой!", 400);
                return;
            }
            Task id = taskManager.getTasks().get(task.getId());
            if (id == null) {
                taskManager.addTask(task);
                writeResponse(exchange, "Задача добавлена!", 201);
                return;
            }
            taskManager.updateTask(task);
            writeResponse(exchange, "Задача обновлена", 200);

        } catch (JsonSyntaxException e) {
            writeResponse(exchange, "Получен некорректный JSON", 400);
        } catch (Exception exp) {
            writeResponse(exchange, "Обнаружено пересечение по времени!", 406);
        }
    }

    private void deleteTask(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();

        if (query == null) {
            writeResponse(exchange, "Не указан id задачи ", 404);
            return;
        }
        if (getTaskId(exchange).isEmpty()) {
            writeResponse(exchange, "Не указан id задачи ", 404);
            return;
        }
        int id = getTaskId(exchange).get();
        if (taskManager.getTasks(id) == null) {
            writeResponse(exchange, "Задач с таким id " + id + " не найдено!", 404);
            return;
        }
        taskManager.deleteTask(id);
        writeResponse(exchange, "Задача удалена!", 200);
    }

}