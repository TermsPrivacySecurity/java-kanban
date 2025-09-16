package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.TaskManager;

import java.io.IOException;

import static task.TaskType.*;

public class EpicsHandler extends BaseHttpHandler implements HttpHandler {

    public EpicsHandler(TaskManager manager) {
        super.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        switch (BaseHttpHandler.getEndpoint(exchange)) {
            case GET_EPICS -> handleGetTasks(exchange, EPIC);
            case GET_EPIC_BY_ID -> handleGetTaskById(exchange, EPIC);
            case GET_SUBTASKS_BY_EPIC -> handleGetSubtasksByEpic(exchange);
            case POST_EPIC -> handlePostTask(exchange, EPIC);
            case DELETE_EPIC -> handleDeleteTask(exchange, EPIC);
            case UNKNOWN -> sendResponse(exchange, 404, "Incorrect endpoint.");
        }
    }

    private void handleGetSubtasksByEpic(HttpExchange exchange) throws IOException {
        try {
            int id = Integer.parseInt(exchange.getRequestURI().getPath().split("/")[2]);
            String response = gson.toJson(manager.getSubtaskListByEpic(id));
            sendText(exchange, response);
        } catch (NumberFormatException e) {
            sendResponse(exchange, 400, "Bad request: incorrect EPIC ID.");
        } catch (NullPointerException e) {
            sendResponse(exchange, 404, "EPIC not found.");
        }
    }
}
