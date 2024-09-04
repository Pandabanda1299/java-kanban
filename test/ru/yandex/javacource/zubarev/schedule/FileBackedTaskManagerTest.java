package ru.yandex.javacource.zubarev.schedule;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.javacource.zubarev.schedule.manager.FileBackedTaskManager;
import ru.yandex.javacource.zubarev.schedule.task.ProgressTask;
import ru.yandex.javacource.zubarev.schedule.task.Task;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class FileBackedTaskManagerTest extends TestTaskManager<FileBackedTaskManager> {


    @BeforeEach
    public void setUp() throws IOException {
        file = File.createTempFile("test", ".csv");
        manager = new FileBackedTaskManager(file);
    }

    @Test
    public void testSaveAndLoad() throws IOException {
        Task task = new Task(1, "Задача 1", "Описание", ProgressTask.NEW, LocalDateTime.now(), Duration.ofMinutes(30));
        manager.addTask(task);

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(file);
        Task loadedTask = loadedManager.getTask(task.getId());

        assertEquals(task, loadedTask);
    }


    @Test
    public void testToString() throws IOException {
        LocalDateTime now = LocalDateTime.now();
        Task task = new Task(1, "Задача 1", "Описание", ProgressTask.NEW, now, Duration.ofMinutes(30));
        manager.addTask(task);
        String expected =
                task.getId() + ",TASK,Задача 1,NEW,Описание," + now.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + "," + 30 + "," + task.getEndTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        String actual = manager.toTaskString(task);

        assertEquals(expected, actual);
    }


}
