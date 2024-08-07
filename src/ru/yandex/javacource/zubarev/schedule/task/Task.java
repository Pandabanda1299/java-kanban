package ru.yandex.javacource.zubarev.schedule.task;

import java.util.Objects;

public class Task {
    private int id;
    private String name;
    private String description;
    private ProgressTask progress;

    public Task(String description, String name) {
        this.description = description;
        this.progress = ProgressTask.NEW;
        this.name = name;
    }

    public Task(String description, String name, int id) {
        this.description = description;
        this.progress = ProgressTask.NEW;
        this.name = name;
        this.id = id;
    }

    public Task(String description, String name, ProgressTask progress) {
        this.description = description;
        this.name = name;
        this.progress = progress;
    }

    public Task(Task task) {
        this.id = task.id;
        this.name = task.name;
        this.description = task.description;
        this.progress = task.progress;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProgressTask getProgress() {
        return progress;
    }

    public void setProgress(ProgressTask progress) {
        this.progress = progress;
    }

    @Override
    public String toString() {
        return "Task{" +
                "description='" + description + '\'' +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", progress=" + progress +
                '}';
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Task other = (Task) obj;
        return id == other.id
                && Objects.equals(name, other.name)
                && Objects.equals(description, other.description)
                && progress == other.progress;
    }


    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, progress);
    }
}
