package com.filip.smartbank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.cassandra.CassandraDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.cassandra.CassandraReactiveDataAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude={CassandraDataAutoConfiguration.class, CassandraReactiveDataAutoConfiguration.class})
@EnableScheduling
public class SmartbankApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartbankApplication.class, args);
    }

}
