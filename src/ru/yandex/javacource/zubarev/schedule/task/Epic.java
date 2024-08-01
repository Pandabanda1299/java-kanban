package ru.yandex.javacource.zubarev.schedule.task;

import java.util.ArrayList;

public class Epic extends Task {


    private ArrayList<Integer> subTasks = new ArrayList<>();

    public Epic(String descriptionTask, String nameTask) {
        super(descriptionTask, nameTask);
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
        return "Epic{" +
                "descriptionTask = '" + getDescription() +
                "', nameTask='" + getName() +
                "', idTask = " + getId() +
                ", subTasks = " + subTasks +
                ", progressTask = " + getProgress() +
                '}';
    }

}