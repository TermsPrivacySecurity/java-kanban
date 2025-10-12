package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.TaskManager;
import task.TaskType;

import java.io.IOException;

public class TasksHandler extends BaseHttpHandler implements HttpHandler {

    public TasksHandler(TaskManager manager) {
        super.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        switch (BaseHttpHandler.getEndpoint(exchange)) {
            case GET_TASKS -> handleGetTasks(exchange, TaskType.TASK);
            case GET_TASK_BY_ID -> handleGetTaskById(exchange, TaskType.TASK);
            case POST_TASK -> handlePostTask(exchange, TaskType.TASK);
            case DELETE_TASK -> handleDeleteTask(exchange, TaskType.TASK);
            case UNKNOWN -> sendResponse(exchange, 404, "Incorrect endpoint.");
        }
    }


}
