package ru.yandex.javacource.zubarev.schedule.task;

public class SubTask extends Task {

    private int epicId;

    public SubTask(String descriptionTask, String nameTask, int idEpicTask) {
        super(descriptionTask, nameTask);
        this.epicId = idEpicTask;
    }

    public SubTask(SubTask subTask) {
        super(subTask);
        this.epicId = subTask.epicId;
    }


    public SubTask(String descriptionTask, String nameTask) {
        super(descriptionTask, nameTask);
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
                '}';
    }
}
