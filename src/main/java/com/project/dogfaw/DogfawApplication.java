package com.project.dogfaw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@EnableScheduling
@EnableJpaAuditing // 생성 시간/수정 시간 자동 업데이트
@SpringBootApplication
public class DogfawApplication {

    @PostConstruct
    void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
    }

    public static void main(String[] args) {
        SpringApplication.run(DogfawApplication.class, args);
    }

//    public static final String APPLICATION_LOCATIONS = "spring.config.location="
//            + "classpath:application.properties,"
//            + "classpath:application.yml,"
//            + "classpath:aws.yml";
//
//    public static void main(String[] args) {
//        new SpringApplicationBuilder(DogfawApplication.class)
//                .properties(APPLICATION_LOCATIONS)
//                .run(args);
//    }

}
