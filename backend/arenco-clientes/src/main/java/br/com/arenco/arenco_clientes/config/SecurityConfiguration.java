package br.com.arenco.arenco_clientes.config;

import br.com.arenco.arenco_clientes.config.properties.ArencoClientesCorsProperties;
import br.com.arenco.arenco_clientes.filters.ArencoPreSessionCookieAuthenticationFilter;
import br.com.arenco.arenco_clientes.filters.ArencoRequestContextFilter;
import br.com.arenco.arenco_clientes.filters.ArencoSessionCookieAuthenticationFilter;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

  private static final PasswordEncoder arencoPasswordEncoder = new BCryptPasswordEncoder();

  private final ArencoPreSessionCookieAuthenticationFilter
      arencoPreSessionCookieAuthenticationFilter;
  private final ArencoClientesCorsProperties corsProperties;
  private final UserDetailsService arencoUserDetailsServiceImpl;
  private final ArencoRequestContextFilter arencoRequestContextFilter;
  private final ArencoSessionCookieAuthenticationFilter arencoSessionCookieAuthenticationFilter;

  @Bean
  public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(
            authorize ->
                authorize
                    .requestMatchers(
                        "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "/webjars/**")
                    .permitAll()
                    .requestMatchers("/actuator/health")
                    .permitAll()
                    .requestMatchers("/actuator/loggers/**")
                    .hasRole("ADMIN")
                    .requestMatchers("/auth/login")
                    .permitAll()
                    .requestMatchers("/customer/auth/**")
                    .permitAll()
                    .requestMatchers("/password-reset")
                    .permitAll()
                    .requestMatchers("/password-reset/**")
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .addFilterBefore(arencoRequestContextFilter, UsernamePasswordAuthenticationFilter.class)
        .addFilterBefore(
            arencoPreSessionCookieAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .addFilterBefore(
            arencoSessionCookieAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .logout(logout -> logout.clearAuthentication(true))
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.NEVER))
        .csrf(AbstractHttpConfigurer::disable)
        .cors(cors -> cors.configurationSource(request -> corsConfiguration()));
    return http.build();
  }

  @Autowired
  public void configureGlobal(final AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(arencoUserDetailsServiceImpl).passwordEncoder(arencoPasswordEncoder);
  }

  @Bean
  public CorsConfiguration corsConfiguration() {
    final var cors = new CorsConfiguration();
    cors.setAllowedOrigins(corsProperties.getCorsAllowedOrigins());
    cors.setAllowedMethods(corsProperties.getCorsAllowedMethods());
    cors.setAllowedHeaders(corsProperties.getCorsAllowedHeaders());
    cors.setAllowCredentials(true);
    cors.setExposedHeaders(List.of("Content-Disposition", "Content-Length", "Content-Type"));
    cors.setMaxAge(3600L);
    return cors;
  }

  @Bean
  public ProviderManager arencoProviderManager(
      final DaoAuthenticationProvider arencoDaoAuthenticationProvider) {
    final var providers = new ArrayList<AuthenticationProvider>();
    providers.add(arencoDaoAuthenticationProvider);
    final var manager = new ProviderManager(providers);
    manager.setEraseCredentialsAfterAuthentication(false);
    return manager;
  }

  @Bean
  public DaoAuthenticationProvider arencoDaoAuthenticationProvider(
      final UserDetailsService arencoUserDetailsService) {
    final var daoAuthenticationProvider = new DaoAuthenticationProvider(arencoUserDetailsService);
    daoAuthenticationProvider.setPasswordEncoder(arencoPasswordEncoder);
    return daoAuthenticationProvider;
  }
}
