package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.TaskManager;

import java.io.IOException;

public class PrioritizedHandler extends BaseHttpHandler implements HttpHandler {

    public PrioritizedHandler(TaskManager manager) {
        super.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        switch (BaseHttpHandler.getEndpoint(exchange)) {
            case GET_PRIORITIZED -> handleGetPrioritized(exchange);
            case UNKNOWN -> sendResponse(exchange, 404, "Incorrect endpoint.");
        }
    }

    private void handleGetPrioritized(HttpExchange exchange) throws IOException {
        String response = gson.toJson(manager.getPrioritizedTasks());
        sendText(exchange, response);
    }
}
