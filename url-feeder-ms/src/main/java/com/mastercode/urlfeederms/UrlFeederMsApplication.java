package com.mastercode.urlfeederms;

import com.mastercode.urlfeederms.model.Properties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
@EnableConfigurationProperties(Properties.class)
public class UrlFeederMsApplication {

    public static void main(String[] args) {
        SpringApplication.run(UrlFeederMsApplication.class, args);
    }

}
