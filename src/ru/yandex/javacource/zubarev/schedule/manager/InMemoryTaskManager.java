package ru.yandex.javacource.zubarev.schedule.manager;

import org.w3c.dom.Node;
import ru.yandex.javacource.zubarev.schedule.task.Epic;
import ru.yandex.javacource.zubarev.schedule.task.ProgressTask;
import ru.yandex.javacource.zubarev.schedule.task.SubTask;
import ru.yandex.javacource.zubarev.schedule.task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {

    private final Map<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, SubTask> subTasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private static int generatorId = 0;
    private final HistoryManager historyManager = Managers.getDefaultHistory();
    private HistoryNode head;
    private HistoryNode tail;



    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(this.tasks.values());
    }

    @Override
    public ArrayList<Task> getEpics() {
        return new ArrayList<>(this.tasks.values());
    }

    @Override
    public ArrayList<Task> getSubTasks() {
        return new ArrayList<>(this.tasks.values());
    }

    @Override
    public int addTask(Task task) {
        int id = ++generatorId;
        task.setId(id);
        final Task newTask = new Task(task);
        tasks.put(newTask.getId(), newTask);
        return id;
    }

    @Override
    public int addEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            return -1;
        }
        int id = ++generatorId;
        epic.setId(id);
        final Epic newEpic = new Epic(epic);
        epics.put(newEpic.getId(), newEpic);
        return id;
    }


    @Override
    public int addSubTask(SubTask subTask) {
        int idEpicTask = subTask.getIdEpic();
        int idSubTask = subTask.getId();
        Epic epic = epics.get(idEpicTask);
        // Генерация нового идентификатора для подзадачи
        int id = ++generatorId;
        subTask.setId(id);
        if (idEpicTask == idSubTask) {
            return -1;
        }
        // Добавление подзадачи в список подзадач эпика
        epic.getSubTasks().add(id);
        subTask.setEpic(epic);
        // Добавление подзадачи в хранилище подзадач
        final SubTask newSubTask = new SubTask(subTask);
        subTasks.put(newSubTask.getId(), newSubTask);
        // Обновление статуса эпика
        updateEpicStatus(idEpicTask);
        return id;
    }


    @Override
    public Task getTask(int id) {
        Task task = tasks.get(id);
        historyManager.add(task);
        return task;
    }


    @Override
    public Epic getEpic(int id) {
        Epic epic = epics.get(id);
        historyManager.add(epic);
        return epic;
    }


    @Override
    public SubTask getSubTask(int id) {
        SubTask subTask = subTasks.get(id);
        historyManager.add(subTask);
        return subTask;
    }


    public void updateEpic(Epic epic) {
        final Epic savedEpic = epics.get(epic.getId());
        if (savedEpic == null) {
            return;
        }
        epic.getSubTasks().clear();
        epic.getSubTasks().addAll(savedEpic.getSubTasks());
        epics.put(epic.getId(), epic);
    }


    @Override
    public void updateSubtask(SubTask subtask) {
        final int id = subtask.getId();
        final int epicId = subtask.getIdEpic();
        final SubTask savedSubtask = subTasks.get(id);
        if (savedSubtask == null) {
            return;
        }
        final Epic epic = epics.get(epicId);
        if (epic == null) {
            return;
        }
        subTasks.put(id, subtask);
        updateEpicStatus(epicId);
    }


    @Override
    public void updateTask(Task task) {
        int id = task.getId();
        Task savedTask = tasks.get(id);
        if (savedTask == null) {
            return;
        }
        tasks.put(id, task);
    }


    @Override
    public void deleteTask(int id) {
        tasks.remove(id);
        historyManager.remove(id);

    }


    @Override
    public void deleteEpic(int id) {
        for (SubTask subTask : subTasks.values()) {
            if (subTask.getIdEpic() == id) {
                subTasks.remove(subTask.getId());
            }
        }
        epics.remove(id);


    }


    @Override
    public void deleteSubtask(int id) {
        SubTask subTask = subTasks.remove(id);
        if (subTask == null) {
            return;
        }
        Epic epic = epics.get(subTask.getIdEpic());
        if (epic != null) {
            updateEpicStatus(epic.getId());
        }
    }




    @Override
    public ArrayList<SubTask> getTasks(Epic epic) {
        ArrayList<SubTask> subTasksForEpic = new ArrayList<>();
        for (SubTask subTask : subTasks.values()) {
            if (subTask.getIdEpic() == epic.getId()) {
                subTasksForEpic.add(subTask);
            }
        }
        return subTasksForEpic;
    }



    private void updateEpicStatus(int epicId) {
        Epic epic = epics.get(epicId);
        if (epic == null) {
            return;
        }

        int doneCounter = 0;
        int newCounter = 0;
        for (SubTask subTask : subTasks.values()) {
            if (subTask.getIdEpic() == epicId) {
                if (subTask.getProgress() == ProgressTask.NEW) {
                    newCounter++;
                }
                if (subTask.getProgress() == ProgressTask.DONE) {
                    doneCounter++;
                }
            }
        }

        if (newCounter == epic.getSubTasks().size()) {
            epic.setProgress(ProgressTask.NEW);
        } else if (doneCounter == epic.getSubTasks().size()) {
            epic.setProgress(ProgressTask.DONE);
        } else {
            epic.setProgress(ProgressTask.IN_PROGRESS);
        }
    }



}





