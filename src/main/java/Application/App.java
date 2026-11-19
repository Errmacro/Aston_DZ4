package Application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"Application/controller", "Application/dto", "Application/entity", "Application/repository", "Application/service", "Application/util"})
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}

