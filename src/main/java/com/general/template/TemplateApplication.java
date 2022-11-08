package com.general.template;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.general.template.**.**.mapper")
//@EnableAsync(proxyTargetClass = true)
@SpringBootApplication(scanBasePackages = "com.general.template")
public class TemplateApplication {

    public static void main(String[] args) {
        SpringApplication.run(TemplateApplication.class, args);
    }

}
