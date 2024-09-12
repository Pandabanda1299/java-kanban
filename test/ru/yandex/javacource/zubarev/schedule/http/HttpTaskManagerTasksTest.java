package ru.yandex.javacource.zubarev.schedule.http;

import com.google.gson.Gson;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.javacource.zubarev.schedule.manager.InMemoryTaskManager;
import ru.yandex.javacource.zubarev.schedule.manager.TaskManager;
import ru.yandex.javacource.zubarev.schedule.server.BaseHttpHandler;
import ru.yandex.javacource.zubarev.schedule.server.HttpTaskServer;
import ru.yandex.javacource.zubarev.schedule.task.Epic;
import ru.yandex.javacource.zubarev.schedule.task.ProgressTask;
import ru.yandex.javacource.zubarev.schedule.task.SubTask;
import ru.yandex.javacource.zubarev.schedule.task.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskManagerTasksTest {

    // создаём экземпляр InMemoryTaskManager
    TaskManager manager = new InMemoryTaskManager();
    // передаём его в качестве аргумента в конструктор HttpTaskServer
    HttpTaskServer taskServer = new HttpTaskServer(manager);
    BaseHttpHandler baseHttpHandler = new BaseHttpHandler(manager);
    Gson gson = baseHttpHandler.getGson();

    public HttpTaskManagerTasksTest() throws IOException {
    }

    @BeforeEach
    public void setUp() throws IOException {
        manager.deleteTask(1);
        manager.deleteSubtask(1);
        manager.deleteEpic(1);
        taskServer.start();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }

    @Test
    public void testAddTask() throws IOException, InterruptedException {
        // создаём задачу
        Task task = new Task(1, "Test 2", "Test description", ProgressTask.NEW, LocalDateTime.now(), Duration.ofMinutes(30));
        // конвертируем её в JSON
        String taskJson = gson.toJson(task);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(201, response.statusCode());

        // проверяем, что создалась одна задача с корректным именем
        List<Task> tasksFromManager = manager.getTasks();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Test 2", tasksFromManager.get(0).getName(), "Некорректное имя задачи");
    }

    @Test
    public void addEpicTest() throws IOException, InterruptedException {
        Epic epic = new Epic(1, "Epic1", "Описание эпика", ProgressTask.NEW,
                new ArrayList<>(), LocalDateTime.now(), Duration.ofMinutes(10), LocalDateTime.now().plusMinutes(10));
        String json = gson.toJson(epic);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        assertNotNull(manager, "Список пуст");
        assertEquals("Epic1", manager.getEpic(epic.getId()).getName(), "Некорректное имя задачи");
    }

    @Test
    public void addSubTask() throws IOException, InterruptedException {
        Epic epic = new Epic(1, "Epic1", "Описание эпика", ProgressTask.NEW,
                new ArrayList<>(), LocalDateTime.now(), Duration.ofMinutes(10), LocalDateTime.now().plusMinutes(10));
        manager.addEpic(epic);
        SubTask subTask = new SubTask(2, "Описание", "Подзадача 1",
                ProgressTask.NEW, epic.getId(), Duration.ofMinutes(30), LocalDateTime.now());
        String json = gson.toJson(subTask);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        assertNotNull(manager, "Список пуст");
        System.out.println(manager.getSubTask(subTask.getId()));
        assertEquals("Подзадача 1", manager.getSubTask(subTask.getId()).getName(), "Некорректное имя задачи");

    }

    @Test
    public void testDeleteTask() throws IOException, InterruptedException {
        Task task = new Task(1, "Задача 1", "Описание",
                ProgressTask.NEW, LocalDateTime.now(), Duration.ofMinutes(30));
        String taskJson = gson.toJson(task);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        URI urlForDelete = URI.create("http://localhost:8080/tasks/1");
        HttpRequest requestForDelete = HttpRequest.newBuilder()
                .uri(urlForDelete)
                .DELETE()
                .build();
        HttpResponse<String> responseDelete = client.send(requestForDelete, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, responseDelete.statusCode());
        assertEquals("Задача удалена!", responseDelete.body());
        assertTrue(manager.getTasks().isEmpty(), "Задача не удалена");


    }

    @Test
    public void testDeleteEpic() throws IOException, InterruptedException {
        Epic epic = new Epic(1, "Эпик", "Описание эпика", ProgressTask.NEW,
                new ArrayList<>(), LocalDateTime.now(), Duration.ofMinutes(10), LocalDateTime.now().plusMinutes(10));
        String epicJson = gson.toJson(epic);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(epicJson))
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        URI urlForDelete = URI.create("http://localhost:8080/epics/1");
        HttpRequest requestForDelete = HttpRequest.newBuilder()
                .uri(urlForDelete)
                .DELETE()
                .build();
        HttpResponse<String> responseDelete = client.send(requestForDelete, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, responseDelete.statusCode());
        assertEquals("Задача удалена!", responseDelete.body());
        assertTrue(manager.getEpics().isEmpty(), "Задача не удален");
    }

    @Test
    public void testDeleteSubTask() throws IOException, InterruptedException {
        Epic epic = new Epic(1, "Epic1", "Описание эпика", ProgressTask.NEW,
                new ArrayList<>(), LocalDateTime.now(), Duration.ofMinutes(10), LocalDateTime.now().plusMinutes(10));
        manager.addEpic(epic);
        SubTask subTask = new SubTask(2, "Описание", "Подзадача 1",
                ProgressTask.NEW, epic.getId(), Duration.ofMinutes(30), LocalDateTime.now());
        String json = gson.toJson(subTask);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(url)
                .DELETE()
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        URI urlForDelete = URI.create("http://localhost:8080/tasks/1");
        HttpRequest requestForDelete = HttpRequest.newBuilder()
                .uri(urlForDelete)
                .DELETE()
                .build();
        HttpResponse<String> responseDelete = client.send(requestForDelete, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, responseDelete.statusCode());
        assertEquals("Задача удалена!", responseDelete.body());
        assertTrue(manager.getSubTasks().isEmpty(), "задача не удалена");

    }
}