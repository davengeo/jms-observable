package org.daven.rx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication
@EnableJms
public class JmsObservableApplication {

    public static void main(String[] args) {
        SpringApplication.run(JmsObservableApplication.class, args);
    }
}
