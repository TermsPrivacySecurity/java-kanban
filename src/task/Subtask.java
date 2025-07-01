package task;

public class Subtask extends Task {
    private int epicId;

    public Subtask(String name, String description, Status status, int epicId) {
        super(name, description, status);
        this.epicId = epicId;
    }

    @Override
    public Type getType() {
        return Type.SUBTASK;
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%s,%s,%s,%s", getId(), getType(), getName(), getStatus(),
                getDescription(), epicId);
    }

    @Override
    public Task returnCopy() {
        Task subtask = new Subtask(this.getName(), this.getDescription(), this.getStatus(), this.epicId);
        subtask.setId(this.getId());
        return subtask;
    }

    public int getEpicId() {
        return epicId;
    }

    public static Subtask fromString(String string) {
        String[] split = string.split(",");
        Subtask subtask = new Subtask(split[2], split[4], Status.valueOf(split[3]), Integer.parseInt(split[5]));
        subtask.setId(Integer.parseInt(split[0]));
        return subtask;
    }
}
