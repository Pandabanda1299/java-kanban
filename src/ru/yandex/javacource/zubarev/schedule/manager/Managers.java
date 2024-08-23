package ru.yandex.javacource.zubarev.schedule.manager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Managers {

    public static TaskManager getDefault() {
        try {
            Path file = Path.of("src/resources/savedTask.csv");
            Path dir = Path.of("src/resources");
            if (!Files.exists(dir)) {
                Files.createDirectory(dir);
                Files.createFile(file);
            }
            if (!Files.exists(file)) {
                Files.createFile(file);
            }
            return new FileBackedTaskManager(file.toFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}




