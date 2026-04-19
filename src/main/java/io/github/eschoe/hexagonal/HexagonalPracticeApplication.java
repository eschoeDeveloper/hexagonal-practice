package io.github.eschoe.hexagonal;

import io.github.eschoe.hexagonal.common.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class HexagonalPracticeApplication extends SpringBootServletInitializer {

    /**
     * WAS( Tomcat ) 배포 시 추가
     * */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(HexagonalPracticeApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(HexagonalPracticeApplication.class, args);
    }

}
