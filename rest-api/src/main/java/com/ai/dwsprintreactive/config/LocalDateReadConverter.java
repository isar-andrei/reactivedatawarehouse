package com.ai.dwsprintreactive.config;

import com.mongodb.lang.NonNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;

@ReadingConverter
public class LocalDateReadConverter implements Converter<Date, LocalDate> {
    @Override
    public LocalDate convert(@NonNull Date source) {
        return source.toInstant().atZone(ZoneOffset.UTC).toLocalDate();
    }
}
