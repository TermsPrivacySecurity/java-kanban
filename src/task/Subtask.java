package task;

public class Subtask extends Task {
    private int epicId;

    public Subtask(String name, String description, int epicId) {
        super(name, description);
        this.epicId = epicId;
        this.type = TaskType.SUBTASK;
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%s,%s,%s,%s", getId(), getType(), getName(), getStatus(),
                getDescription(), epicId);
    }

    @Override
    public Task returnCopy() {
        Task subtask = new Subtask(this.getName(), this.getDescription(), this.epicId);
        subtask.setId(this.getId());
        subtask.setStatus(this.getStatus());
        return subtask;
    }

    public int getEpicId() {
        return epicId;
    }

    public static Subtask fromString(String string) {
        String[] split = string.split(",");
        Subtask subtask = new Subtask(split[2], split[4], Integer.parseInt(split[5]));
        subtask.setId(Integer.parseInt(split[0]));
        subtask.setStatus(TaskStatus.valueOf(split[3]));
        return subtask;
    }
}
