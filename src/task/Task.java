package task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task {
    private String name;
    private String description;
    private TaskStatus status;
    protected TaskType type;
    private int id;
    private Duration duration;
    private LocalDateTime startTime;
    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    public Task(String name, String description, long duration, LocalDateTime startTime) {
        this.name = name;
        this.description = description;
        this.status = TaskStatus.NEW;
        this.type = TaskType.TASK;
        this.duration = Duration.ofMinutes(duration);
        this.startTime = startTime;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public int getId() {
        return id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public TaskType getType() {
        return type;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return startTime.plus(duration);
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%s,%s,%s,%s,%s", id, getType(), name, status, description, duration.toMinutes(),
                startTime.format(formatter));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public Task returnCopy() {
        Task task = new Task(this.name, this.description, this.duration.toMinutes(), this.startTime);
        task.id = this.id;
        task.status = this.status;
        return task;
    }

    public static Task fromString(String string) {
        String[] split = string.split(",");
        Task task = new Task(split[2], split[4], Long.parseLong(split[5]),
                LocalDateTime.parse(split[6], formatter));
        task.id = Integer.parseInt(split[0]);
        task.status = TaskStatus.valueOf(split[3]);
        return task;
    }
}
