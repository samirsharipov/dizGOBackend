package uz.pdp.springsecurity.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import uz.pdp.springsecurity.security.JwtFilter;
import uz.pdp.springsecurity.service.AuthService;

import java.util.List;

@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final AuthService authService;
    private final JwtFilter jwtFilter;

    @Bean
    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    @Primary  // Buni asosiy menejer qilib belgilash
    public AuthenticationManager userAuthenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(authService);  // User uchun
        return authenticationManagerBuilder.build();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(authService);  // Default User uchun autentifikatsiya
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();  // Default menejerni qaytarish
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // slesh belgisini qoyish kk apidaan oldin
                .cors().and()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(
                        "/api/auth/**",
                        "/api/product-export/*",
                        "/api/v1/hr/auth/login",
                        "/swagger-ui/**",
                        "/swagger-resources/**",
                        "/api/attachment/download/*",
                        "/v2/api-docs",
                        "/api/business/create",
                        "/api/business/checkBusinessName",
                        "/api/business/checkUsername",
                        "/api/tariff/getToChooseATariff",
                        "/api/notification/*",
                        "/api/tariff/getById/*",
                        "/api/attachment/**",
                        "/api/files/**",
                        "/api/form-lid/*",
                        "/api/role/get-role-tariff/*",
                        "/api/select-for-lid/getByBusinessId/*",
                        "/api/form-lid/get-form/*",
                        "/api/lid",
                        "/api/files/*",
                        "/api/customer/export/*",
                        "/api/customer/import/*",
                        "/app/topic/public",
                        "/app/chat.send",
                        "/app/message",
                        "/app/chatroom/public",
                        "/ws",
                        "/api/product-excel/**",
                        "/api/language/**",
                        "/api/customer/registration",
                        "/api/verification/**",
                        "/api/customer/check-number",
                        "/api/user/get-by-phone-number/*"
                )
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.sessionManagement().maximumSessions(1).sessionRegistry(sessionRegistry());
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Bean
    public ServletListenerRegistrationBean<HttpSessionEventPublisher> httpSessionEventPublisher() {
        return new ServletListenerRegistrationBean<>(new HttpSessionEventPublisher());
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CorsFilter corsFilter() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("*"));
//        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://localhost:3001", "http://165.232.126.177:3000"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return new CorsFilter(source);
    }
}
