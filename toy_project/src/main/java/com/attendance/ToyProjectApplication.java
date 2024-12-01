package com.attendance;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.attendance.dao") // 매퍼 인터페이스 패키지를 지정합니다.
public class ToyProjectApplication {

  public static void main(String[] args) {
    SpringApplication.run(ToyProjectApplication.class, args);
  }

}
