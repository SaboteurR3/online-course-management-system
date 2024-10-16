package com.task.onlinecoursemanagementsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class OnlineCourseManagementSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlineCourseManagementSystemApplication.class, args);
    }

}
