package com.digiup.sandbox;

import com.digiup.sandbox.model.Msg;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(Application.class, args);
        Msg msg = ctx.getBean(Msg.class);
        msg.process("this is test");
    }

}
