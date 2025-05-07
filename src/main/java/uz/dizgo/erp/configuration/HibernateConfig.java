package uz.dizgo.erp.configuration;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import uz.dizgo.erp.payload.UserDTO;
import uz.dizgo.erp.utils.UserSession;

import java.util.Optional;
import java.util.UUID;

@Configuration
@EnableJpaAuditing
public class HibernateConfig implements AuditorAware<UUID> {

    private final UserSession userSession;

    public HibernateConfig(UserSession userSession) {
        this.userSession = userSession;
    }


//    @NotNull
//    @Override
//    public Optional<UUID> getCurrentAuditor() {
//        return userSession.getUser() != null ? Optional.ofNullable(userSession.getUser().getId()) : null;
//    }
    @NotNull
    public Optional<UUID> getCurrentAuditor() {
        UserDTO user = userSession.getUser();
        return Optional.ofNullable(user).map(UserDTO::getId);
    }
}
