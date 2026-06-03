package bg.nbu.medialrecordapp.security;

import bg.nbu.medialrecordapp.ui.view.auth.LoginView;
import com.vaadin.flow.spring.security.VaadinSavedRequestAwareAuthenticationSuccessHandler;
import com.vaadin.flow.spring.security.VaadinSecurityConfigurer;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) {

        http.setSharedObject(
                VaadinSavedRequestAwareAuthenticationSuccessHandler.class,
                new SuccessHandler()
        );

        http.with(VaadinSecurityConfigurer.vaadin(), configurer -> {
            configurer.loginView(LoginView.class);
        });

        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private static class SuccessHandler
            extends VaadinSavedRequestAwareAuthenticationSuccessHandler {

        @Override
        protected String determineTargetUrl(
                HttpServletRequest request,
                HttpServletResponse response,
                Authentication authentication
        ) {
            return "/";
        }
    }
}