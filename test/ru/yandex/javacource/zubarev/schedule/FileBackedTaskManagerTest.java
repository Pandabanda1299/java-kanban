package ru.yandex.javacource.zubarev.schedule;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.javacource.zubarev.schedule.manager.FileBackedTaskManager;
import ru.yandex.javacource.zubarev.schedule.task.ProgressTask;
import ru.yandex.javacource.zubarev.schedule.task.Task;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest {

    private FileBackedTaskManager manager;
    private File file;

    @BeforeEach
    public void setUp() throws IOException {
        file = new File("test_tasks.csv");
        manager = new FileBackedTaskManager(file.getName());
    }

    @Test
    public void testSaveAndLoad() throws IOException {
        Task task = new Task("Test Task", "Description", ProgressTask.NEW);
        manager.addTask(task);
        manager.save();

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(file);
        Task loadedTask = loadedManager.getTask(task.getId());

        assertEquals(task, loadedTask);
    }

    @Test
    public void testToString() {
        Task task = new Task("Test Task", "Description", ProgressTask.NEW);
        manager.addTask(task);

        String expected = "id,type,name,status,description,epic\n" +
                task.getId() + ",TASK,Test Task,NEW,Description,\n";
        assertEquals(expected, manager.toString());
    }
}
