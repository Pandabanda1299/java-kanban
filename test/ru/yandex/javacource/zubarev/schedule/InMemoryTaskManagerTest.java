package ru.yandex.javacource.zubarev.schedule;

import org.junit.jupiter.api.BeforeEach;
import ru.yandex.javacource.zubarev.schedule.manager.InMemoryTaskManager;

public class InMemoryTaskManagerTest extends TestTaskManager<InMemoryTaskManager> {

    @BeforeEach
    void setUp() {
        manager = new InMemoryTaskManager();
    }

}
