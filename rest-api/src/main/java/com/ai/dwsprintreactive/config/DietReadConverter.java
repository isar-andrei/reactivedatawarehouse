package com.ai.dwsprintreactive.config;

import com.ai.dwsprintreactive.model.*;
import io.r2dbc.spi.Row;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@ReadingConverter
public class DietReadConverter implements Converter<Row, Diet> {

    public Diet convert(Row row) {
        return new Diet();
    }
}
