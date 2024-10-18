package manager;

import task.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {
    private final ArrayList<Task> history = new ArrayList<>();
    private final int maxHistorySize = 10;

    @Override
    public void add(Task task) {
        if (history.size() >= maxHistorySize) {
            history.removeFirst();
        }
        history.add(task.returnCopy());
    }

    @Override
    public ArrayList<Task> getHistory() {
        return history;
    }
}
