package ru.yandex.javacource.zubarev.schedule.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.javacource.zubarev.schedule.manager.TaskManager;
import ru.yandex.javacource.zubarev.schedule.task.SubTask;
import ru.yandex.javacource.zubarev.schedule.task.Task;

import javax.xml.datatype.Duration;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Optional;

import static java.util.Objects.isNull;


public class SubtaskHandler extends BaseHttpHandler implements HttpHandler {
    private  Gson gson;
    private  TaskManager taskManager;
    String response;

    public SubtaskHandler(TaskManager taskManager, Gson gson) {
        this.taskManager = taskManager;
        this.gson = new GsonBuilder()
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter())
                .create();
    }


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        switch (method) {
            case "GET":
                getSubTask(exchange);
                break;
            case "POST":
                addSubTask(exchange);
                break;
            case "DELETE":
                deleteSubTask(exchange);
                break;
            default:
                writeResponse(exchange, "Такой операции не существует", 404);
        }
    }

    private void getSubTask(HttpExchange exchange) throws IOException {
        if (exchange.getRequestURI().getQuery() == null) {
            response = gson.toJson(taskManager.getSubTasks());
            writeResponse(exchange, response, 200);
            return;
        }

        Optional<Integer> optionalId = getTaskId(exchange);
        if (optionalId.isEmpty()) {
            writeResponse(exchange, "Некорректный идентификатор", 400);
            return;
        }

        int id = optionalId.get();
        SubTask subTask = taskManager.getSubTask(id);
        if (subTask == null) {
            writeResponse(exchange, "Подзадачи с id " + id + " не найдено!", 404);
            return;
        }
        response = gson.toJson(subTask);
        writeResponse(exchange, response, 200);
    }


    private void addSubTask(HttpExchange exchange) throws IOException {
        try {
            InputStream json = exchange.getRequestBody();
            String jsonTask = new String(json.readAllBytes(), DEFAULT_CHARSET);
            SubTask subTask = gson.fromJson(jsonTask, SubTask.class);
            if (subTask == null) {
                writeResponse(exchange, "Подзадача не должна быть пустой!", 400);
                return;
            }
            SubTask existingSubTask = (SubTask) taskManager.getSubTask(subTask.getId());
            if (existingSubTask == null) {
                int newId = taskManager.addSubTask(subTask);
                writeResponse(exchange, "Подзадача добавлена с id: " + newId, 201);
                return;
            }
            taskManager.updateSubtask(subTask);
            writeResponse(exchange, "Подзадача обновлена", 200);

        } catch (JsonSyntaxException e) {
            writeResponse(exchange, "Получен некорректный JSON", 400);
        } catch (Exception exp) {
            writeResponse(exchange, "Обнаружено пересечение по времени или другая ошибка!", 406);
        }
    }


    private void deleteSubTask(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();

        if (query == null) {
            writeResponse(exchange, "Не указан id подзадачи", 404);
            return;
        }
        if (getTaskId(exchange).isEmpty()) {
            writeResponse(exchange, "Не указан id подзадачи", 404);
            return;
        }
        int id = getTaskId(exchange).get();
        if (taskManager.getSubTask(id) == null) {
            writeResponse(exchange, "Подзадача с id " + id + " не найдена!", 404);
            return;
        }
        taskManager.deleteSubtask(id);
        writeResponse(exchange, "Подзадача удалена!", 200);
    }

}