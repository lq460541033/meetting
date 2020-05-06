package com.swf.attence;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@MapperScan("com.swf.attence.mapper")
public class AttenceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AttenceApplication.class, args);
    }

}

