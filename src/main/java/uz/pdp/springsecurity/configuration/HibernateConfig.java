package uz.pdp.springsecurity.configuration;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import uz.pdp.springsecurity.utils.UserSession;

import java.util.Optional;
import java.util.UUID;

@Configuration
@EnableJpaAuditing
public class HibernateConfig implements AuditorAware<UUID> {

    private final UserSession userSession;

    public HibernateConfig(UserSession userSession) {
        this.userSession = userSession;
    }

    @NotNull
    @Override
    public Optional<UUID> getCurrentAuditor() {
        return Optional.ofNullable(userSession.getUser() != null ? userSession.getUser().getId() : null);
    }
}
