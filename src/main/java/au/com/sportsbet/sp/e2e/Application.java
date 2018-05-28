package au.com.sportsbet.sp.e2e;

import lombok.SneakyThrows;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;

import java.nio.file.Paths;

import static org.springframework.boot.SpringApplication.run;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        run(Application.class, args);
    }

    public static ClassPathResource resolveClasspathResource(String resourceName) {
        return new ClassPathResource(resourceName.replace("classpath:", ""));
    }

    @SneakyThrows
    public static String resolveClasspathResourceAbsolutePath(String resourceName) {
        return Paths.get(resolveClasspathResource(resourceName).getURI()).toFile().getAbsolutePath();
    }

}
