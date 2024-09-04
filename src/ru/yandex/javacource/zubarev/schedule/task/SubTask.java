package ru.yandex.javacource.zubarev.schedule.task;

import java.time.Duration;
import java.time.LocalDateTime;

public class SubTask extends Task {

    private int epicId;

    public SubTask(int id, String descriptionTask, String nameTask, ProgressTask progress, int idEpicTask, Duration durationTask, LocalDateTime start) {
        super(descriptionTask, nameTask, ProgressTask.NEW);
        this.epicId = idEpicTask;
        this.setDuration(durationTask);
        this.setStartTime(start);
    }

    public SubTask(SubTask subTask) {
        super(subTask);
        this.epicId = subTask.epicId;
    }

    public SubTask(String name, String description, ProgressTask progress, int epicId, Duration duration, LocalDateTime startTime) {
        super(name, description, progress);
        this.epicId = epicId;
        setDuration(duration);
        setStartTime(startTime);
    }


    public SubTask(int id, String name, String description, ProgressTask progress, int epicId) {
        super(id, name, description, progress);
        this.epicId = epicId;
    }


    public void setEpic(Epic epic) {
        this.epicId = epic.getId();
    }

    public int getIdEpic() {
        return epicId;
    }


    public void setIdEpicTask(int idEpicTask) {
        this.epicId = idEpicTask;
    }


    @Override
    public String toString() {
        return "SubTask{" +
                "descriptionTask='" + getDescription() +
                "', nameTask='" + getName() +
                "', idEpicTask=" + epicId +
                ", progressTask=" + getProgress() +
                ", idSubTask=" + getId() +
                ", taskType = " + TaskType.SUBTASK +
                ", duration=" + getDuration() +
                ", startTime=" + getStartTime() +
                '}';
    }
}
