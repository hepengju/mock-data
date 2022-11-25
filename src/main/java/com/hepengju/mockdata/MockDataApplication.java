package com.hepengju.mockdata;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class MockDataApplication {

    public static void main(String[] args) {
        SpringApplication.run(MockDataApplication.class, args);
    }

}
