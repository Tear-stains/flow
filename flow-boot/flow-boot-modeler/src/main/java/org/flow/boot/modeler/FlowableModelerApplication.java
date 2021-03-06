package org.flow.boot.modeler;

import org.flow.boot.modeler.config.MyAppDispatcherServletConfiguration;
import org.flow.boot.modeler.config.MyApplicationConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Import;

@Import({ MyApplicationConfiguration.class, MyAppDispatcherServletConfiguration.class })
@SpringBootApplication
public class FlowableModelerApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {

        SpringApplication.run(FlowableModelerApplication.class, args);

    }
}