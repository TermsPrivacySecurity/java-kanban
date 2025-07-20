package task;

import java.util.Objects;

public class Task {
    private String name;
    private String description;
    private TaskStatus status;
    protected TaskType type;
    private int id;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = TaskStatus.NEW;
        this.type = TaskType.TASK;
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

    @Override
    public String toString() {
        return String.format("%s,%s,%s,%s,%s", id, getType(), name, status, description);
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
        Task task = new Task(this.name, this.description);
        task.id = this.id;
        task.status = this.status;
        return task;
    }

    public static Task fromString(String string) {
        String[] split = string.split(",");
        Task task = new Task(split[2], split[4]);
        task.id = Integer.parseInt(split[0]);
        task.status = TaskStatus.valueOf(split[3]);
        return task;
    }
}
