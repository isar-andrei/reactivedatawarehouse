package com.ai.dwsprintreactive.config;


import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.postgresql.client.SSLMode;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableR2dbcRepositories
@EnableConfigurationProperties
public class PostgresConfig extends AbstractR2dbcConfiguration {

    @Override
    @Bean
    public ConnectionFactory connectionFactory() {
        return new PostgresqlConnectionFactory(
                PostgresqlConnectionConfiguration.builder()
                        .host("localhost")
                        .username("postgres")
                        .password("")
                        .database("datawarehouse")
                       // .sslMode(SSLMode.REQUIRE)
                        .build());
    }

    @Bean
    @Override
    public R2dbcCustomConversions r2dbcCustomConversions() {
        List<Converter<?, ?>> converterList = new ArrayList<Converter<?, ?>>();
       //converterList.add(new DietReadConverter());
       // converterList.add(new DietWriteConverter());

        return new R2dbcCustomConversions(getStoreConversions(), converterList);
    }
}
