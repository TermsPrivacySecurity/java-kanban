package manager;

import task.Task;

import java.util.Objects;

class Node {
    Task task;
    Node next;
    Node previous;

    Node(Task task, Node next, Node previous) {
        this.task = task;
        this.next = next;
        this.previous = previous;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return Objects.equals(task, node.task);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(task);
    }
}
