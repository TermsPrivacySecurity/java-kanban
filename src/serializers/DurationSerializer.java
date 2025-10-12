package serializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;

import java.lang.reflect.Type;
import java.time.Duration;

public class DurationSerializer implements JsonSerializer<Duration>, JsonDeserializer<Duration> {

    @Override
    public JsonElement serialize(Duration src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.getSeconds());
    }

    @Override
    public Duration deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        return Duration.ofSeconds(json.getAsLong());
    }
}