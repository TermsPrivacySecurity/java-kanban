package task;

import java.util.Objects;

public class Task {
    private String name;
    private String description;
    private Status status;
    private int id;

    public Task(String name, String description, Status status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    public int getId() {
        return id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(Status status) {
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

    public Type getType() {
        return Type.TASK;
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
        Task task = new Task(this.name, this.description, this.status);
        task.id = this.id;
        return task;
    }

    public static Task fromString(String string) {
        String[] split = string.split(",");
        Task task = new Task(split[2], split[4], Status.valueOf(split[3]));
        task.id = Integer.parseInt(split[0]);
        return task;
    }
}
