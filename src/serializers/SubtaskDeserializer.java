package serializers;

import com.google.gson.*;
import task.Subtask;
import task.TaskStatus;

import java.lang.reflect.Type;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class SubtaskDeserializer implements JsonDeserializer<Subtask> {

    @Override
    public Subtask deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String name = jsonObject.get("name").getAsString();
        String description = jsonObject.get("description").getAsString();
        TaskStatus status = TaskStatus.NEW;
        int epicId = jsonObject.get("epicId").getAsInt();
        if (jsonObject.has("status") && !jsonObject.get("status").isJsonNull()) {
            status = TaskStatus.valueOf(jsonObject.get("status").getAsString());
        }
        int id = 0;
        if (jsonObject.has("id") && !jsonObject.get("id").isJsonNull()) {
            id = jsonObject.get("id").getAsInt();
        }
        Duration duration = Duration.ofSeconds(300);
        if (jsonObject.has("duration") && !jsonObject.get("duration").isJsonNull()) {
            duration = Duration.ofSeconds(jsonObject.get("duration").getAsLong());
        }
        LocalDateTime startTime = LocalDateTime.ofInstant(Instant.now(), ZoneId.of("+05:00"));
        if (jsonObject.has("startTime") && !jsonObject.get("startTime").isJsonNull()) {
            startTime = LocalDateTime.parse(jsonObject.get("startTime").getAsString(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        }

        Subtask subtask = new Subtask(name, description, epicId);
        subtask.setStatus(status);
        subtask.setId(id);
        subtask.setDuration(duration);
        subtask.setStartTime(startTime);
        return subtask;
    }
}