package uz.dizgo.erp;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaRepositories("uz.dizgo.erp")
@EnableScheduling
@EnableCaching
public class DizGOApplication {
    public static void main(String[] args) {
        SpringApplication.run(DizGOApplication.class, args);
    }

}