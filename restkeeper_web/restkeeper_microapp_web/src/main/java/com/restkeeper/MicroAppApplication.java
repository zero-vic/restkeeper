package com.restkeeper;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class},scanBasePackages = {"com.restkeeper"})
@EnableDiscoveryClient
public class MicroAppApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(MicroAppApplication.class)
                .properties("app.type=microApp")
                .run(args);
    }

    /*public static void main(String[] args) {
        SpringApplication.run(MicroAppApplication.class,args);
    }*/

}
