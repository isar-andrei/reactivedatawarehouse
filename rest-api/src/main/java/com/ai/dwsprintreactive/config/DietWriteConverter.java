package com.ai.dwsprintreactive.config;


import com.ai.dwsprintreactive.model.Diet;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.r2dbc.mapping.OutboundRow;
import org.springframework.data.r2dbc.mapping.SettableValue;

@WritingConverter
public class DietWriteConverter implements Converter<Diet, OutboundRow> {

    public OutboundRow convert(Diet source) {
        return new OutboundRow();
    }
}
