package ru.yandex.javacource.zubarev.schedule.manager;

import ru.yandex.javacource.zubarev.schedule.task.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final File file;
    private String fileName;

    public FileBackedTaskManager(File file) {
        this.file = file;
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
        save();
    }

    @Override
    public void updateSubtask(SubTask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
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
        final int id = super.addSubTask(subTask);
        save();
        return id;
    }


    @Override
    public int addEpic(Epic epic) {
        final int id = super.addEpic(epic);
        save();
        return id;
    }

    @Override
    public int addTask(Task task) {
        final int id = super.addTask(task);
        save();
        return id;
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

    private static final String HEADER = "id,type,name,status,description,epic";


    public static String toString(Task task) {
        StringBuilder sb = new StringBuilder();
        sb.append(task.getId()).append(",")
                .append(task.getType()).append(",")
                .append(task.getName()).append(",")
                .append(task.getProgress()).append(",")
                .append(task.getDescription());

        if (task.getType().equals(TaskType.SUBTASK)) {
            sb.append(",").append(((SubTask) task).getIdEpic());
        }

        return sb.toString();
    }


    protected void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(HEADER);
            writer.newLine();

            for (Map.Entry<Integer, Task> entry : tasks.entrySet()) {
                final Task task = entry.getValue();
                writer.write(toString(task));
                writer.newLine();
            }

            for (Map.Entry<Integer, SubTask> entry : subTasks.entrySet()) {
                final Task task = entry.getValue();
                writer.write(toString(task));
                writer.newLine();
            }

            for (Map.Entry<Integer, Epic> entry : epics.entrySet()) {
                final Task task = entry.getValue();
                writer.write(toString(task));
                writer.newLine();
            }

        } catch (IOException e) {
            throw new ManagerSaveException("Can't save to file: " + file.getName(), e);
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
        final FileBackedTaskManager taskManager = new FileBackedTaskManager(file);
        try {
            final String csv = Files.readString(file.toPath());
            final String[] lines = csv.split(System.lineSeparator());
            int generatorId = 0;
            for (int i = 1; i < lines.length; i++) {
                String line = lines[i];
                if (line.isEmpty()) {
                    break;
                }
                final Task task = fromString(line);
                final int id = task.getId();
                if (id > generatorId) {
                    generatorId = id;
                }
                taskManager.addAnyTask(task);
            }
            for (Map.Entry<Integer, SubTask> e : taskManager.subTasks.entrySet()) {
                final SubTask subtask = e.getValue();
                final Epic epic = taskManager.epics.get(subtask.getIdEpic());
                epic.getSubTasks().add(subtask.getId());
            }
            taskManager.generatorId = generatorId;
        } catch (IOException e) {
            throw new ManagerSaveException("Can't read form file: " + file.getName(), e);
        }
        return taskManager;
    }


    protected void addAnyTask(Task task) {
        final int id = task.getId();
        switch (task.getType()) {
            case TASK:
                tasks.put(id, task);
                break;
            case SUBTASK:
                subTasks.put(id, (SubTask) task);
                break;
            case EPIC:
                epics.put(id, (Epic) task);
                break;
        }
    }


}

