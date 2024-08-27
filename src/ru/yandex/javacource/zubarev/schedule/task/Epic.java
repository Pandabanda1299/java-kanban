package ru.yandex.javacource.zubarev.schedule.task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class Epic extends Task {

    private LocalDateTime endTime;
    private List<Integer> subTasks = new ArrayList<>();

    public Epic(String nameTask, String descriptionTask) {
        super(0, nameTask, descriptionTask, ProgressTask.NEW);
        this.endTime = LocalDateTime.now();
    }

    public Epic(int id, String name, String description, ProgressTask progress, List<Integer> subTasks) {
        super(id, name, description, progress);
        this.subTasks = new ArrayList<>(subTasks);
        this.endTime = LocalDateTime.now();
    }

    public Epic(int id, String name, String description, ProgressTask progress, List<Integer> subTask, LocalDateTime start, Duration durationTask, LocalDateTime endTime) {
       super(id, name, description, progress,start, durationTask);
        this.subTasks = subTask;
        this.endTime = endTime;
    }

    public List<Integer> getSubTasks() {
        return subTasks;
    }

    public void setSubTasks(List<Integer> subTasks) {
        this.subTasks = subTasks;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }


    public void removeSubtask(int id) {
        subTasks.remove(Integer.valueOf(id));
    }


    public Epic(Epic epic) {
        super(epic);
        this.subTasks = epic.subTasks;
        this.endTime = epic.endTime;
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