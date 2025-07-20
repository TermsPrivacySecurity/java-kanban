package task;

import java.util.ArrayList;

public class Epic extends Task {
    private final ArrayList<Integer> subtaskIds;

    public Epic(String name, String description) {
        super(name, description);
        subtaskIds = new ArrayList<>();
        this.type = TaskType.EPIC;
    }

    @Override
    public Task returnCopy() {
        Task epic = new Epic(this.getName(), this.getDescription());
        epic.setStatus(this.getStatus());
        epic.setId(this.getId());
        return epic;
    }

    public ArrayList<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    public static Epic fromString(String string) {
        String[] split = string.split(",");
        Epic epic = new Epic(split[2], split[4]);
        epic.setId(Integer.parseInt(split[0]));
        epic.setStatus(TaskStatus.valueOf(split[3]));
        return epic;
    }
}
