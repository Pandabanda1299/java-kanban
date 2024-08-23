package ru.yandex.javacource.zubarev.schedule;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.javacource.zubarev.schedule.manager.FileBackedTaskManager;
import ru.yandex.javacource.zubarev.schedule.task.ProgressTask;
import ru.yandex.javacource.zubarev.schedule.task.Task;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class FileBackedTaskManagerTest {

    private FileBackedTaskManager manager;
    private File file;

    @BeforeEach
    public void setUp() throws IOException {
        file = File.createTempFile("test", ".csv");
        manager = new FileBackedTaskManager(file);
    }

    @Test
    public void testSaveAndLoad() throws IOException {
        Task task = new Task("Description", "Name", ProgressTask.NEW);
        manager.addTask(task);

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(file);
        Task loadedTask = loadedManager.getTask(task.getId());

        assertEquals(task, loadedTask);
    }


    @Test
    public void testToString() throws IOException {
        Task task = new Task("Description", "Test Task", ProgressTask.NEW);
        manager.addTask(task);
        String expected =
                task.getId() + ",TASK,Test Task,NEW,Description";
        String actual = manager.toString(task);

        assertEquals(expected, actual);
    }


}
