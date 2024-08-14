package ru.yandex.javacource.zubarev.schedule.manager;

import ru.yandex.javacource.zubarev.schedule.task.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private String fileName;

    public FileBackedTaskManager(String fileName) {
        super();
        this.fileName = fileName;

    }



    @Override
    public ArrayList<SubTask> getTasks(Epic epic) {
        return super.getTasks(epic);
    }

    @Override
    public void deleteSubtask(int id) {
        super.deleteSubtask(id);
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
    }

    @Override
    public void updateSubtask(SubTask subtask) {
        super.updateSubtask(subtask);
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
    }

    @Override
    public SubTask getSubTask(int id) {
        return super.getSubTask(id);
    }

    @Override
    public Epic getEpic(int id) {
        return super.getEpic(id);
    }

    @Override
    public Task getTask(int id) {
        return super.getTask(id);
    }

    @Override
    public int addSubTask(SubTask subTask) {
        return super.addSubTask(subTask);
    }

    @Override
    public int addEpic(Epic epic) {
        return super.addEpic(epic);
    }

    @Override
    public int addTask(Task task) {
        return super.addTask(task);
    }

    @Override
    public ArrayList<Task> getSubTasks() {
        return super.getSubTasks();
    }

    @Override
    public ArrayList<Task> getEpics() {
        return super.getEpics();
    }

    @Override
    public ArrayList<Task> getTasks() {
        return super.getTasks();
    }

    @Override
    public List<Task> getHistory() {
        return super.getHistory();
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("id,type,name,status,description,epic\n");

        for (Task task : tasks.values()) {
            String type = task instanceof Epic ? "EPIC" : task instanceof SubTask ? "SUBTASK" : "TASK";
            String epic = "";

            if (type.equals("SUBTASK")) {
                epic = String.valueOf(((SubTask) task).getIdEpic());
            }

            sb.append(task.getId()).append(",")
                    .append(type).append(",")
                    .append(task.getName()).append(",")
                    .append(task.getProgress()).append(",")
                    .append(task.getDescription()).append(",")
                    .append(epic).append("\n");
        }
        return sb.toString();
    }



    public void save() {
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(this.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    public static Task fromString(String value) {
        String[] parts = value.split(",");
        int id = Integer.parseInt(parts[0]);
        TaskType type = TaskType.valueOf(parts[1]);
        String name = parts[2];
        ProgressTask progress = ProgressTask.valueOf(parts[3]);
        String description = parts[4];

        switch (type) {
            case TASK:
                return new Task(id, name, description, progress);

            case EPIC:
                List<Integer> subTasks = new ArrayList<>();
                if (parts.length > 5) {
                    String[] subTaskIds = parts[5].split(",");
                    for (String subTaskId : subTaskIds) {
                        subTasks.add(Integer.parseInt(subTaskId));
                    }
                }
                return new Epic(id, name, description, progress, subTasks);
             case SUBTASK:
                int epicId = Integer.parseInt(parts[5]);
                return new SubTask(id, name, description, progress, epicId);
            default:
                throw new IllegalArgumentException("Invalid task type: " + type);
        }
    }


    public static FileBackedTaskManager loadFromFile(File file) {
        if (file == null || !file.exists()) {
            throw new IllegalArgumentException("File is null or does not exist.");
        }
        FileBackedTaskManager manager = new FileBackedTaskManager(file.getName());
        try (Scanner scanner = new Scanner(file)) {
            scanner.nextLine();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                Task task = fromString(line);
                manager.addTaskBasedOnType(task);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return manager;
    }


    private void addTaskBasedOnType(Task task) {
        if (task instanceof Epic) {
            addEpic((Epic) task);
        } else if (task instanceof SubTask) {
            addSubTask((SubTask) task);
        } else {
            addTask(task);
        }
    }


}

