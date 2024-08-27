package ru.yandex.javacource.zubarev.schedule.task;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class Epic extends Task {
    private List<Integer> subTasks = new ArrayList<>();

    public Epic(String nameTask, String descriptionTask) {
        super(0, nameTask, descriptionTask, ProgressTask.NEW);
    }

    public Epic(int id, String name, String description, ProgressTask progress, List<Integer> subTasks) {
        super(id, name, description, progress);
        this.subTasks = new ArrayList<>(subTasks);
    }


    public List<Integer> getSubTasks() {
        return subTasks;
    }

    public void setSubTasks(List<Integer> subTasks) {
        this.subTasks = subTasks;
    }


    public void removeSubtask(int id) {
        subTasks.remove(Integer.valueOf(id));
    }


    public Epic(Epic epic) {
        super(epic);
        this.subTasks = epic.subTasks;
    }

    public LocalDateTime getStartTime() {
        if (subTasks.isEmpty()) {
            return null;
        }
        return subTasks.stream()
                .map(SubTask::getStartTime)
                .min(LocalDateTime::compareTo)
                .orElse(null);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getId()).append(",")
                .append(TaskType.EPIC).append(",")
                .append(getName()).append(",")
                .append(getProgress()).append(",")
                .append(getDescription()).append(",");

        List<Integer> subTaskIds = getSubTasks();
        if (!subTaskIds.isEmpty()) {
            boolean isFirst = true;
            for (Integer subTaskId : subTaskIds) {
                if (isFirst) {
                    sb.append(subTaskId);
                    isFirst = false;
                } else {
                    sb.
                            append(",").append(subTaskId);
                }
            }
        }

        return sb.toString();
    }
}