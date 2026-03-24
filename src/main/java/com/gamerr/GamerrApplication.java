package com.gamerr;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Gamerr 游戏预约系统 - 启动类
 */
@SpringBootApplication
@MapperScan("com.gamerr.mapper")
public class GamerrApplication {

    public static void main(String[] args) {
        SpringApplication.run(GamerrApplication.class, args);
        System.out.println("========================================");
        System.out.println("  Gamerr Backend Started Successfully!");
        System.out.println("  API Address: http://localhost:8080/api");
        System.out.println("========================================");
    }
}
