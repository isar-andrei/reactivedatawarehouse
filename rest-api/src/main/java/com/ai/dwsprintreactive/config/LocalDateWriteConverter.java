package com.ai.dwsprintreactive.config;

import com.mongodb.lang.NonNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;

@WritingConverter
public class LocalDateWriteConverter implements Converter<LocalDate, Date> {
    @Override
    public Date convert(@NonNull LocalDate source) {
        return new Date(source.atStartOfDay().atZone(ZoneOffset.UTC).toInstant().toEpochMilli());
    }
}
