package com.ai.dwsprintreactive.config;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableMongoRepositories(basePackages = "com.ai.dwsprintreactive.repository")
public class MongoConfig extends AbstractReactiveMongoConfiguration {

    private final List<Converter<?, ?>> converters = new ArrayList<Converter<?, ?>>();

    @Value("${spring.data.mongodb.uri}")
    String uri;

    @Value("${spring.data.mongodb.database}")
    String database;

    @Override
    protected String getDatabaseName() {
        return database;
    }

    @Override
    public MongoClient reactiveMongoClient() {
        return MongoClients.create(uri);
    }

    @Override
    public MongoCustomConversions customConversions() {
        converters.add(new LocalDateReadConverter());
        converters.add(new LocalDateWriteConverter());
        converters.add(new LocalDateTimeReadConverter());
        converters.add(new LocalDateTimeWriteConverter());

        return new MongoCustomConversions(converters);
    }

    // DOESNT WORK
//        @Override
//    public boolean autoIndexCreation() {
//        return true;
//    }
}
