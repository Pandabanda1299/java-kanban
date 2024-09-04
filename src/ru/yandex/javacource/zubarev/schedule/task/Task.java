package ru.yandex.javacource.zubarev.schedule.task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task implements Comparable<Task> {
    private int id;
    private String name;
    private String description;
    private ProgressTask progress;
    private Duration duration;
    private LocalDateTime startTime;


    public Task(String description, int id, String name, ProgressTask progress, Duration duration, LocalDateTime startTime) {
        this.description = description;
        this.id = id;
        this.name = name;
        this.progress = progress;
        this.duration = duration;
        this.startTime = startTime;
    }


    public Task(Task task) {
        this.id = task.id;
        this.name = task.name;
        this.description = task.description;
        this.progress = task.progress;
        this.startTime = task.startTime;
        this.duration = task.duration;
    }

    public Task(int id, String name, String description, ProgressTask progress) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.progress = progress;
        this.startTime = LocalDateTime.now();
        this.duration = Duration.ofMinutes(0);
    }

    public Task(String descriptionTask, String nameTask, ProgressTask progress) {
        this.description = descriptionTask;
        this.name = nameTask;
        this.progress = progress;
    }

    public Task(int id, String name, String description, ProgressTask progress, LocalDateTime start, Duration durationTask) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.progress = progress;
        this.duration = durationTask;
        this.startTime = start;
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

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getEndTime() {
        if (startTime == null || duration == null) {
            return null;
        }
        return startTime.plusMinutes(duration.toMinutes());
    }


    @Override
    public String toString() {
        return "Task { " +
                "description ='" + description + '\'' +
                ", id = " + id +
                ", name ='" + name + '\'' +
                ", progress = " + progress +
                ", taskType = " + TaskType.TASK +
                ", duration = " + duration +
                ", startTime = " + (startTime != null ? getStartTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) : null) +
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

    public TaskType getType() {
        if (this instanceof Epic) {
            return TaskType.EPIC;
        } else if (this instanceof SubTask) {
            return TaskType.SUBTASK;
        }
        return TaskType.TASK;

    }

    @Override
    public int compareTo(Task o) {
        return this.startTime.compareTo(o.getStartTime());
    }
}
