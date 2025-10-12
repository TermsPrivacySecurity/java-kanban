package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.TaskManager;

import java.io.IOException;

public class HistoryHandler extends BaseHttpHandler implements HttpHandler {

    public HistoryHandler(TaskManager manager) {
        super.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        switch (BaseHttpHandler.getEndpoint(exchange)) {
            case GET_HISTORY -> handleGetHistory(exchange);
            case UNKNOWN -> sendResponse(exchange, 404, "Incorrect endpoint.");
        }
    }

    private void handleGetHistory(HttpExchange exchange) throws IOException {
        String response = gson.toJson(manager.getHistory());
        sendText(exchange, response);
    }
}
