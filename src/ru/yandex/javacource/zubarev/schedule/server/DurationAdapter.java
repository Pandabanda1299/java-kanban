package ru.yandex.javacource.zubarev.schedule.server;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.Duration;
import java.time.format.DateTimeFormatter;

public class DurationAdapter extends TypeAdapter<Duration> {
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    @Override
    public void write(JsonWriter jsonWriter, final Duration duration) throws IOException {
    if (duration == null) {
        jsonWriter.nullValue();
       } else jsonWriter.value(duration.toMinutes());
     }


    @Override
    public Duration read(final JsonReader jsonReader) throws IOException {
        return Duration.ofMinutes(Long.parseLong(jsonReader.nextString()));
    }
}
