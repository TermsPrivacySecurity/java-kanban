package server;

import serializers.DurationSerializer;
import serializers.LocalDateTimeSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import exceptions.HasInteractionException;
import manager.TaskManager;
import task.Epic;
import task.Subtask;
import task.Task;
import task.TaskType;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;

public class BaseHttpHandler {
    protected TaskManager manager;
    protected Gson gson = new GsonBuilder()
            .registerTypeAdapter(Duration.class, new DurationSerializer())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer())
            .create();

    protected static Endpoint getEndpoint(HttpExchange exchange) {
        String path = exchange.getRequestURI().getPath();
        String[] splitPath = path.split("/");
        String method = exchange.getRequestMethod();

        if (splitPath.length == 2 && splitPath[1].equals("tasks") && method.equals("GET")) {
            return Endpoint.GET_TASKS;
        }
        if (splitPath.length == 3 && splitPath[1].equals("tasks") && method.equals("GET")) {
            return Endpoint.GET_TASK_BY_ID;
        }
        if (splitPath.length == 2 && splitPath[1].equals("subtasks") && method.equals("GET")) {
            return Endpoint.GET_SUBTASKS;
        }
        if (splitPath.length == 3 && splitPath[1].equals("subtasks") && method.equals("GET")) {
            return Endpoint.GET_SUBTASK_BY_ID;
        }
        if (splitPath.length == 2 && splitPath[1].equals("epics") && method.equals("GET")) {
            return Endpoint.GET_EPICS;
        }
        if (splitPath.length == 3 && splitPath[1].equals("epics") && method.equals("GET")) {
            return Endpoint.GET_EPIC_BY_ID;
        }
        if (splitPath.length == 4 && splitPath[1].equals("epics") && splitPath[3].equals("subtasks")
                && method.equals("GET")) {
            return Endpoint.GET_SUBTASKS_BY_EPIC;
        }
        if (splitPath.length == 2 && splitPath[1].equals("history") && method.equals("GET")) {
            return Endpoint.GET_HISTORY;
        }
        if (splitPath.length == 2 && splitPath[1].equals("prioritized") && method.equals("GET")) {
            return Endpoint.GET_PRIORITIZED;
        }
        if (splitPath.length == 2 && splitPath[1].equals("tasks") && method.equals("POST")) {
            return Endpoint.POST_TASK;
        }
        if (splitPath.length == 2 && splitPath[1].equals("subtasks") && method.equals("POST")) {
            return Endpoint.POST_SUBTASK;
        }
        if (splitPath.length == 2 && splitPath[1].equals("epics") && method.equals("POST")) {
            return Endpoint.POST_EPIC;
        }
        if (splitPath.length == 3 && splitPath[1].equals("tasks") && method.equals("DELETE")) {
            return Endpoint.DELETE_TASK;
        }
        if (splitPath.length == 3 && splitPath[1].equals("subtasks") && method.equals("DELETE")) {
            return Endpoint.DELETE_SUBTASK;
        }
        if (splitPath.length == 3 && splitPath[1].equals("epics") && method.equals("DELETE")) {
            return Endpoint.DELETE_EPIC;
        }
        return Endpoint.UNKNOWN;
    }

    protected void handleGetTasks(HttpExchange exchange, TaskType type) throws IOException {
        String response = switch (type) {
            case TASK -> gson.toJson(manager.getTasksList());
            case SUBTASK -> gson.toJson(manager.getSubtasksList());
            case EPIC -> gson.toJson(manager.getEpicsList());
        };
        sendText(exchange, response);
    }

    protected void handleGetTaskById(HttpExchange exchange, TaskType type) throws IOException {
        try {
            int id = Integer.parseInt(exchange.getRequestURI().getPath().split("/")[2]);
            String response = switch (type) {
                case TASK -> gson.toJson(manager.getTaskById(id));
                case SUBTASK -> gson.toJson(manager.getSubtaskById(id));
                case EPIC -> gson.toJson(manager.getEpicById(id));
            };
            sendText(exchange, response);
        } catch (NumberFormatException e) {
            sendResponse(exchange, 400, "Bad request: incorrect " + type + " ID.");
        } catch (NullPointerException e) {
            sendResponse(exchange, 404, type + " not found.");
        }
    }

    protected void handlePostTask(HttpExchange exchange, TaskType type) throws IOException {
        try {
            String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            if (body.isBlank()) {
                throw new JsonSyntaxException("Body is empty.");
            }
            switch (type) {
                case TASK -> {
                    Task task = gson.fromJson(body, Task.class);
                    if (task.getId() == 0) {
                        manager.addNewTask(task);
                    } else {
                        manager.updateTasks(task);
                    }
                }
                case SUBTASK -> {
                    Subtask subtask = gson.fromJson(body, Subtask.class);
                    if (subtask.getId() == 0) {
                        manager.addNewSubtask(subtask);
                    } else {
                        manager.updateSubtasks(subtask);
                    }
                }
                case EPIC -> {
                    Epic epic = gson.fromJson(body, Epic.class);
                    if (epic.getId() == 0) {
                        manager.addNewEpic(epic);
                    } else {
                        manager.updateEpics(epic);
                    }
                }
            }
            sendResponse(exchange, 201, "Success.");
        } catch (JsonSyntaxException e) {
            sendResponse(exchange, 400, "Incorrect request body." + e.getMessage());
        } catch (NullPointerException e) {
            sendResponse(exchange, 404, type + " not found.");
        } catch (HasInteractionException e) {
            sendResponse(exchange, 406, e.getMessage());
        }
    }

    protected void handleDeleteTask(HttpExchange exchange, TaskType type) throws IOException {
        try {
            int id = Integer.parseInt(exchange.getRequestURI().getPath().split("/")[2]);
            switch (type) {
                case TASK -> manager.removeTaskById(id);
                case SUBTASK -> manager.removeSubtaskById(id);
                case EPIC -> manager.removeEpicById(id);
            }
            sendResponse(exchange, 200, "Success.");
        } catch (NumberFormatException e) {
            sendResponse(exchange, 400, "Bad request: incorrect " + type + " ID.");
        } catch (NullPointerException e) {
            sendResponse(exchange, 404, type + " not found.");
        }
    }

    protected void sendText(HttpExchange exchange, String text) throws IOException {
        byte[] response = text.getBytes(StandardCharsets.UTF_8);
        System.out.println(text);
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(200, response.length);
        exchange.getResponseBody().write(response);
        exchange.close();
    }

    protected void sendResponse(HttpExchange exchange, int rCode, String text) throws IOException {
        byte[] response = text.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(rCode, response.length);
        exchange.getResponseBody().write(response);
        exchange.close();
    }

}