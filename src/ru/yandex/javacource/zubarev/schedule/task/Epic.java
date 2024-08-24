package ru.yandex.javacource.zubarev.schedule.task;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


public class Epic extends Task {


    private List<Integer> subTasks = new ArrayList<>();
    private Duration duration;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public Epic(String descriptionTask, String nameTask) {
        super(descriptionTask, nameTask);
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

    public void updateTimeAndDuration() {
        List<Integer> subTasks = getSubTasks();
        if (subTasks.isEmpty()) {
            setDuration(new Duration(0));
            setStartTime(null);
            return;
        }

        Optional<LocalDateTime> earliestStart = subTasks.stream()
                .map(subTasks::getStartTime)
                .filter(Objects::nonNull)
                .min(LocalDateTime::compareTo);

        Optional<LocalDateTime> latestEnd = subTasks.stream()
                .map(SubTask::getEndTime)
                .filter(Objects::nonNull)
                .max(LocalDateTime::compareTo);

        int totalMinutes = subTasks.stream()
                .map(SubTask::getDuration)
                .filter(Objects::nonNull)
                .mapToInt(Duration::getMinutes)
                .sum();

        setStartTime(earliestStart.orElse(null));
        setDuration(new Duration(totalMinutes));
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
                    sb.append(",").append(subTaskId);
                }
            }
        }

        return sb.toString();
    }


}