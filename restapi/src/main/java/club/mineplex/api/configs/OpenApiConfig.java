package club.mineplex.api.configs;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(
        title = "MineplexService",
        version = "0.0.1",
        description = "")
)
public class OpenApiConfig {
}
