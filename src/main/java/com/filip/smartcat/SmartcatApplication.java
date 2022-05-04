package com.filip.smartcat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.cassandra.CassandraDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.cassandra.CassandraReactiveDataAutoConfiguration;

@SpringBootApplication(exclude={CassandraDataAutoConfiguration.class, CassandraReactiveDataAutoConfiguration.class})
public class SmartcatApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartcatApplication.class, args);
    }

}
