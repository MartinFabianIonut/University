package ppd.server.myserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class MyServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyServerApplication.class, args);
    }
}
