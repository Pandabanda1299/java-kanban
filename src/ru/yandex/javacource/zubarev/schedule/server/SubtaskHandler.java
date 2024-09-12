package ru.yandex.javacource.zubarev.schedule.server;

import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.javacource.zubarev.schedule.manager.TaskManager;
import ru.yandex.javacource.zubarev.schedule.task.SubTask;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;


public class SubtaskHandler extends BaseHttpHandler implements HttpHandler {
    String response;

    public SubtaskHandler(TaskManager taskManager) {
        super(taskManager);
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
            SubTask existingSubTask = taskManager.getSubTask(subTask.getId());
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
        String path = exchange.getRequestURI().getPath();
        String[] ids = path.split("/");
        int id = 0;

        if (ids.length > 2) {
            id = Integer.parseInt(ids[2]);
        }

        if (id == 0) {
            writeResponse(exchange, "Не указан id подзадачи ", 404);
            return;
        }

        if (taskManager.getSubTasks() == null) {
            writeResponse(exchange, "Задач с таким id " + id + " не найдено!", 404);
            return;
        }

        taskManager.deleteSubtask(id);
        writeResponse(exchange, "Задача удалена!", 200);
    }

}