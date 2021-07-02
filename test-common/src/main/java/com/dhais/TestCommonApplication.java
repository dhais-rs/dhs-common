package com.dhais;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * All rights Reserved, Designed By Fan Jun
 *
 * @author Fan Jun
 * @since 2021/3/11 15:07
 */
@SpringBootApplication
@MapperScan("com.dhais.mapper")
public class TestCommonApplication {
    public static void main(String[] args) {
        SpringApplication.run(TestCommonApplication.class, args);
    }
}
