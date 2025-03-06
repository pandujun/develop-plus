package io.github.pandujun.develop.plus.core.enums;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.github.pandujun.develop.plus.core.constant.DatePatternConstant;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

/**
 * gson创建
 * </P>
 * &#064;Author pandujun
 * </P>
 * &#064;Date 2024/12/27
 */
public enum GsonSingleton {
    INSTANCE;

    public Gson getGson() {
        return (new GsonBuilder())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
                .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter()).
                registerTypeAdapter(LocalTime.class, new LocalTimeTypeAdapter()).create();
    }

    /**
     * localDateTime 适配器
     */
    private static class LocalDateTimeTypeAdapter extends TypeAdapter<LocalDateTime> {
        @Override
        public void write(JsonWriter out, LocalDateTime value) throws IOException {
            if (Objects.nonNull(value)) {
                String localDateTimeStr = value.format(DatePatternConstant.Y_M_D_H_M_S_NORMAL_PATTERN);
                out.value(localDateTimeStr);
            } else {
                out.nullValue();
            }
        }

        @Override
        public LocalDateTime read(JsonReader jsonReader) throws IOException {
            String value = jsonReader.nextString();
            if (Objects.nonNull(value) && !value.trim().isEmpty()) {
                return LocalDateTime.parse(value, DatePatternConstant.Y_M_D_H_M_S_NORMAL_PATTERN);
            }else {
                return null;
            }
        }
    }

    /**
     * localDate 适配器
     */
    private static class LocalDateTypeAdapter extends TypeAdapter<LocalDate> {
        @Override
        public void write(JsonWriter out, LocalDate value) throws IOException {
            if (Objects.nonNull(value)) {
                String localDateTimeStr = value.format(DatePatternConstant.Y_M_D_NORMAL_PATTERN);
                out.value(localDateTimeStr);
            } else {
                out.nullValue();
            }
        }

        @Override
        public LocalDate read(JsonReader jsonReader) throws IOException {
            String value = jsonReader.nextString();
            if (Objects.nonNull(value) && !value.trim().isEmpty()) {
                return LocalDate.parse(value, DatePatternConstant.Y_M_D_NORMAL_PATTERN);
            }else {
                return null;
            }
        }
    }

    /**
     * localTime 适配器
     */
    private static class LocalTimeTypeAdapter extends TypeAdapter<LocalTime> {
        @Override
        public void write(JsonWriter out, LocalTime value) throws IOException {
            if (Objects.nonNull(value)) {
                String localDateTimeStr = value.format(DatePatternConstant.H_M_S_NORMAL_PATTERN);
                out.value(localDateTimeStr);
            } else {
                out.nullValue();
            }
        }

        @Override
        public LocalTime read(JsonReader jsonReader) throws IOException {
            String value = jsonReader.nextString();
            if (Objects.nonNull(value) && !value.trim().isEmpty()) {
                return LocalTime.parse(value, DatePatternConstant.H_M_S_NORMAL_PATTERN);
            }else {
                return null;
            }
        }
    }

}
