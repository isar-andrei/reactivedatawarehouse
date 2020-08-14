package com.ai.dwsprintreactive.config;

import com.ai.dwsprintreactive.model.Diet;
import io.r2dbc.spi.Row;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

@ReadingConverter
public class DietReadConverter implements Converter<Row, Diet> {

    public Diet convert(Row row) {
        return Diet.builder().build();
    }
}
