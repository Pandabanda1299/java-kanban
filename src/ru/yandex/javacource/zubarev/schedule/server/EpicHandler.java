package ru.yandex.javacource.zubarev.schedule.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.javacource.zubarev.schedule.manager.TaskManager;
import ru.yandex.javacource.zubarev.schedule.task.Epic;

import javax.xml.datatype.Duration;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Optional;


public class EpicHandler extends BaseHttpHandler implements HttpHandler {
    private  Gson gson;
    private  TaskManager taskManager;
    String response;

    public EpicHandler(TaskManager taskManager, Gson gson) {
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
                getEpic(exchange);
                break;
            case "POST":
                addEpic(exchange);
                break;
            case "DELETE":
                deleteEpic(exchange);
                break;
            default:
                writeResponse(exchange, "Такой операции не существует", 404);
        }
    }

    private void getEpic(HttpExchange exchange) throws IOException {
        if (exchange.getRequestURI().getQuery() == null) {
            response = gson.toJson(taskManager.getEpics());
            writeResponse(exchange, response, 200);
            return;
        }

        Optional<Integer> optionalId = getTaskId(exchange);
        if (optionalId.isEmpty()) {
            writeResponse(exchange, "Некорректный идентификатор", 400);
            return;
        }

        int id = optionalId.get();
        Epic epic = taskManager.getEpic(id);
        if (epic == null) {
            writeResponse(exchange, "Эпик с id " + id + " не найден!", 404);
            return;
        }
        response = gson.toJson(epic);
        writeResponse(exchange, response, 200);
    }

    private void addEpic(HttpExchange exchange) throws IOException {
        try {
            InputStream json = exchange.getRequestBody();
            String jsonTask = new String(json.readAllBytes(), DEFAULT_CHARSET);
            Epic epic = gson.fromJson(jsonTask, Epic.class);
            if (epic == null) {
                writeResponse(exchange, "Эпик не должен быть пустым!", 400);
                return;
            }
            Epic existingEpic = taskManager.getEpic(epic.getId());
            if (existingEpic == null) {
                int newId = taskManager.addEpic(epic);
                writeResponse(exchange, "Эпик добавлен с id: " + newId, 201);
                return;
            }
            taskManager.updateEpic(epic);
            writeResponse(exchange, "Эпик обновлен", 200);

        } catch (JsonSyntaxException e) {
            writeResponse(exchange, "Получен некорректный JSON", 400);
        } catch (Exception exp) {
            writeResponse(exchange, "Произошла ошибка при добавлении или обновлении эпика!", 500);
        }
    }

    private void deleteEpic(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();

        if (query == null) {
            writeResponse(exchange, "Не указан id эпика", 404);
            return;
        }
        if (getTaskId(exchange).isEmpty()) {
            writeResponse(exchange, "Не указан id эпика", 404);
            return;
        }
        int id = getTaskId(exchange).get();
        if (taskManager.getEpic(id) == null) {
            writeResponse(exchange, "Эпик с id " + id + " не найден!", 404);
            return;
        }
        taskManager.deleteEpic(id);
        writeResponse(exchange, "Эпик удален!", 200);
    }

}