package uz.dizgo.erp.configuration;

import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import uz.dizgo.erp.configuration.jpaConfig.AuditorAwareImpl;

import java.util.UUID;


@Configuration
public class JpaConfig {

    @Bean
    public HibernatePropertiesCustomizer hibernateCustomizer() {
        return properties -> {
            String ddlAuto = properties.getOrDefault("hibernate.hbm2ddl.auto", "none").toString();
            if (!ddlAuto.equalsIgnoreCase("update") && !ddlAuto.equalsIgnoreCase("none")) {
                throw new IllegalStateException("❌ `spring.jpa.hibernate.ddl-auto` faqat `update` yoki `none` bo‘lishi kerak!");
            }
            properties.put("hibernate.hbm2ddl.auto", "update"); // ❗ Har doim `none` yoki `update`
        };
    }


    @Bean
    public AuditorAware<UUID> auditorProvider() {
        return new AuditorAwareImpl(); // pastda yozamiz
    }
}
