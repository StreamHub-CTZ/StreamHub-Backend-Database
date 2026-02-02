package com.streamhub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StreamHubApplication {

    public static void main(String[] args) {
        SpringApplication.run(StreamHubApplication.class, args);
        System.out.println("=====================================");
        System.out.println("StreamHub Backend Started Successfully");
        System.out.println("=====================================");
    }
}
