package com.power;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Scanner;

@SpringBootApplication
@MapperScan("com.power.mapper")
@EnableScheduling
public class ShiXiApplication {
    public static void main(String[] args) {
        SpringApplication.run(ShiXiApplication.class, args);
    }
}
