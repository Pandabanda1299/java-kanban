package ru.yandex.javacource.zubarev.schedule;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import ru.yandex.javacource.zubarev.schedule.manager.FileBackedTaskManager;
import ru.yandex.javacource.zubarev.schedule.task.Epic;
import ru.yandex.javacource.zubarev.schedule.task.ProgressTask;
import ru.yandex.javacource.zubarev.schedule.task.SubTask;
import ru.yandex.javacource.zubarev.schedule.task.Task;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

class FileBackedTaskManagerTest {

    @TempDir
    Path tempDir;

    @Test
    void testSaveAndLoadEmptyFile() throws IOException {
        File file = File.createTempFile("tasks", ".csv", tempDir.toFile());
        FileBackedTaskManager manager = new FileBackedTaskManager(file.getAbsolutePath());

        manager.save();
        List<String> lines = Files.readAllLines(file.toPath());
        Assertions.assertEquals(1, lines.size());
        Assertions.assertEquals("id,type,name,status,description,epic", lines.get(0));

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(file);
        Assertions.assertTrue(loadedManager.getTasks().isEmpty());
    }

    @Test
    void testSaveAndLoadMultipleTasks() throws IOException {
        File file = File.createTempFile("tasks", ".csv", tempDir.toFile());
        FileBackedTaskManager manager = new FileBackedTaskManager(file.getAbsolutePath());

        Task task = new Task(1, "Task 1", "Description 1", ProgressTask.NEW);
        Epic epic = new Epic(2, "Epic 2", "Description 2", ProgressTask.IN_PROGRESS, Arrays.asList(3, 4));
        SubTask subTask = new SubTask(3, "SubTask 3", "Description 3", ProgressTask.DONE, 2);

        manager.addTask(task);
        manager.addEpic(epic);
        manager.addSubTask(subTask);

        manager.save();

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(file);
        Assertions.assertEquals(3, loadedManager.getTasks().size());
        Assertions.assertTrue(loadedManager.getTasks().contains(task));
        Assertions.assertTrue(loadedManager.getTasks().contains(epic));
        Assertions.assertTrue(loadedManager.getTasks().contains(subTask));
    }
}
