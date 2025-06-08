package manager;

import task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    private final Map<Integer, Node> history = new HashMap<>();
    private Node first;
    private Node last;

    @Override
    public void add(Task task) {
        remove(task.getId());
        linkLast(task);
    }

    @Override
    public void remove(int id) {
        removeNode(history.remove(id));
    }

    @Override
    public List<Task> getHistory() {
        List<Task> historyList = new ArrayList<>();
        Node node = first;
        while (node != null) {
            historyList.add(node.task);
            node = node.next;
        }
        return historyList;
    }

    public void linkLast(Task task) {
        Node node = new Node(task.returnCopy(), null, last);
        if (first == null) {
            first = node;
        } else {
            last.next = node;
        }
        last = node;
        history.put(task.getId(), node);
    }

    public void removeNode(Node node) {
        if (node == null) {
            return;
        }
        Node previous = node.previous;
        Node next = node.next;
        if (node == first && node == last) {
            first = null;
            last = null;
        } else if (node == first) {
            first = next;
            first.previous = null;
        } else if (node == last) {
            last = previous;
            last.next = null;
        } else {
            previous.next = next;
            next.previous = previous;
        }
    }
}
