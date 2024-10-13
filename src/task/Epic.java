package task;

import java.util.ArrayList;

public class Epic extends Task {
    private final ArrayList<Integer> subtaskIds;

    public Epic(String name, String description) {
        super(name, description, Status.NEW);
        subtaskIds = new ArrayList<>();
    }

    public ArrayList<Integer> getSubtaskIds() {
        return subtaskIds;
    }

}
