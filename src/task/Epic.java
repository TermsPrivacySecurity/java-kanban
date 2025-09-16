package task;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {
    private final ArrayList<Integer> subtaskIds;

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    private LocalDateTime endTime;

    public Epic(String name, String description) {
        super(name, description);
        subtaskIds = new ArrayList<>();
        this.type = TaskType.EPIC;
    }

    @Override
    public Epic returnCopy() {
        Epic epic = new Epic(this.getName(), this.getDescription());
        epic.setStatus(this.getStatus());
        epic.setId(this.getId());
        return epic;
    }

    public ArrayList<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%s,%s,%s", getId(), getType(), getName(), getStatus(), getDescription());
    }

    public static Epic fromString(String string) {
        String[] split = string.split(",");
        Epic epic = new Epic(split[2], split[4]);
        epic.setId(Integer.parseInt(split[0]));
        epic.setStatus(TaskStatus.valueOf(split[3]));
        return epic;
    }
}
