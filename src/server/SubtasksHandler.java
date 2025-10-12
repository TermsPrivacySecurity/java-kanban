package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.TaskManager;
import task.TaskType;

import java.io.IOException;

public class SubtasksHandler extends BaseHttpHandler implements HttpHandler {

    public SubtasksHandler(TaskManager manager) {
        super.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        switch (BaseHttpHandler.getEndpoint(exchange)) {
            case GET_SUBTASKS -> handleGetTasks(exchange, TaskType.SUBTASK);
            case GET_SUBTASK_BY_ID -> handleGetTaskById(exchange, TaskType.SUBTASK);
            case POST_SUBTASK -> handlePostTask(exchange, TaskType.SUBTASK);
            case DELETE_SUBTASK -> handleDeleteTask(exchange, TaskType.SUBTASK);
            case UNKNOWN -> sendResponse(exchange, 404, "Incorrect endpoint.");

        }
    }

}
