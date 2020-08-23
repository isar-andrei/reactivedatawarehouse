package com.ai.dwsprintreactive.config;

import com.mongodb.lang.NonNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@ReadingConverter
public class LocalDateTimeReadConverter implements Converter<Date, LocalDateTime> {
    @Override
    public LocalDateTime convert(@NonNull Date source) {
        return source.toInstant().atZone(ZoneOffset.UTC).toLocalDateTime();
    }
}
