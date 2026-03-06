package br.com.arenco.arenco_clientes.config;

import br.com.arenco.arenco_clientes.config.properties.RateLimitProperties;
import br.com.arenco.arenco_clientes.filters.RateLimitFilter;
import br.com.arenco.arenco_clientes.utils.DurationParser;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(RateLimitProperties.class)
public class RateLimitConfiguration {

  @Bean
  public Cache<@NonNull String, Bucket> rateLimitCache() {
    return Caffeine.newBuilder()
        .expireAfterAccess(30, TimeUnit.MINUTES)
        .maximumSize(100_000)
        .build();
  }

  @Bean
  public Bandwidth rateLimitBandwidth(final RateLimitProperties props) {
    final Duration period = DurationParser.parse(props.getRefillPeriod());
    return Bandwidth.classic(
        props.getCapacity(), Refill.intervally(props.getRefillTokens(), period));
  }

  @Bean
  public FilterRegistrationBean<RateLimitFilter> rateLimitFilterRegistration(
      final Cache<@NonNull String, Bucket> cache,
      final Bandwidth rateLimitBandwidth,
      final RateLimitProperties props) {
    final FilterRegistrationBean<RateLimitFilter> reg = new FilterRegistrationBean<>();
    reg.setFilter(new RateLimitFilter(cache, rateLimitBandwidth, props.getIncludePaths()));
    reg.setOrder(1);
    reg.addUrlPatterns("/*");
    return reg;
  }
}
