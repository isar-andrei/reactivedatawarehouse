package com.ai.dwsprintreactive.config;

import com.mongodb.lang.NonNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@WritingConverter
public class LocalDateTimeWriteConverter implements Converter<LocalDateTime, Date> {
    @Override
    public Date convert(@NonNull LocalDateTime source) {
        return new Date(source.atZone(ZoneOffset.UTC).toInstant().toEpochMilli());
    }
}
