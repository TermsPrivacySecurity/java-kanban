package task;

import java.time.LocalDateTime;

public class Subtask extends Task {
    private final int epicId;

    public Subtask(String name, String description, int epicId, long duration, LocalDateTime startTime) {
        super(name, description, duration, startTime);
        this.epicId = epicId;
        this.type = TaskType.SUBTASK;
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%s,%s,%s,%s,%s,%s", getId(), getType(), getName(), getStatus(),
                getDescription(), epicId, getDuration().toMinutes(), getStartTime().format(formatter));
    }

    @Override
    public Task returnCopy() {
        Task subtask = new Subtask(this.getName(), this.getDescription(),
                this.epicId, this.getDuration().toMinutes(), this.getStartTime());
        subtask.setId(this.getId());
        subtask.setStatus(this.getStatus());
        return subtask;
    }

    public int getEpicId() {
        return epicId;
    }

    public static Subtask fromString(String string) {
        String[] split = string.split(",");
        Subtask subtask = new Subtask(split[2], split[4], Integer.parseInt(split[5]),
                Long.parseLong(split[6]), LocalDateTime.parse(split[7], formatter));
        subtask.setId(Integer.parseInt(split[0]));
        subtask.setStatus(TaskStatus.valueOf(split[3]));
        return subtask;
    }
}
