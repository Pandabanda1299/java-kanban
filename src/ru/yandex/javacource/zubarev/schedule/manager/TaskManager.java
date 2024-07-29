package ru.yandex.javacource.zubarev.schedule.manager;

import ru.yandex.javacource.zubarev.schedule.task.Epic;
import ru.yandex.javacource.zubarev.schedule.task.SubTask;
import ru.yandex.javacource.zubarev.schedule.task.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {

    List<Task> getHistory();

    ArrayList<Task> getTasks();

    ArrayList<Task> getEpics();

    ArrayList<Task> getSubTasks();

    int addTask(Task task);

    int addEpic(Epic epic);

    int addSubTask(SubTask subTask);

    Task getTask(int idTask);

    Epic getEpic(int idTask);

    SubTask getSubTask(int idTask);

    void updateEpic(Epic epic);

    void updateSubtask(SubTask subTask);

    void updateTask(Task task);

    void deleteTask(int idTask);

    void deleteEpic(int idTask);

    void deleteSubtask(int id);

    ArrayList<SubTask> getTasks(Epic epic);
    
}
