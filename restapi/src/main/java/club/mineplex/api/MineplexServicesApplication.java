package club.mineplex.api;

import lombok.SneakyThrows;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MineplexServicesApplication {
    @SneakyThrows
    public static void main(final String[] args) {
        SpringApplication.run(MineplexServicesApplication.class, args);
    }
}
