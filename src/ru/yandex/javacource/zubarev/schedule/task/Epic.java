package ru.yandex.javacource.zubarev.schedule.task;

import java.util.ArrayList;
import java.util.List;


public class Epic extends Task {


    private ArrayList<Integer> subTasks = new ArrayList<>();

    public Epic(String descriptionTask, String nameTask) {
        super(descriptionTask, nameTask);
    }

    public Epic(int id, String name, String description, ProgressTask progress, List<Integer> subTasks) {
        super(id, name, description, progress);
        this.subTasks = new ArrayList<>(subTasks);
    }


    public ArrayList<Integer> getSubTasks() {
        return subTasks;
    }

    public void setSubTasks(ArrayList<Integer> subTasks) {
        this.subTasks = subTasks;
    }


    public void removeSubtask(int id) {
        subTasks.remove(Integer.valueOf(id));
    }


    public Epic(Epic epic) {
        super(epic);
        this.subTasks = epic.subTasks;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getId()).append(",")
                .append(TaskType.EPIC).append(",")
                .append(getName()).append(",")
                .append(getProgress()).append(",")
                .append(getDescription()).append(",");

        List<Integer> subTasks = getSubTasks();
        if (!subTasks.isEmpty()) {
            boolean isFirst = true;
            for (Integer subTaskId : subTasks) {
                if (isFirst) {
                    sb.append(subTaskId);
                    isFirst = false;
                } else {
                    sb.append(",").append(subTaskId);
                }
            }
        }

        return sb.toString();
    }


}