package io.github.mufasa1976.meetup.springboottest.config;

import io.github.mufasa1976.meetup.springboottest.entities.PersonalEventEntity;
import io.github.mufasa1976.meetup.springboottest.repositories.PersonalEventRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

@Configuration
@EnableJpaRepositories(basePackageClasses = PersonalEventRepository.class)
@EntityScan(basePackageClasses = PersonalEventEntity.class)
@EnableJpaAuditing(auditorAwareRef = "lastModifiedBy", dateTimeProviderRef = "lastModifiedAt")
@Slf4j
public class DatabaseConfiguration {
  @Bean
  public AuditorAware<String> lastModifiedBy() {
    return () -> Optional.ofNullable(SecurityContextHolder.getContext())
                         .map(SecurityContext::getAuthentication)
                         .map(Authentication::getName);
  }

  @Bean
  public DateTimeProvider lastModifiedAt() {
    return () -> Optional.of(OffsetDateTime.now(ZoneOffset.UTC)); // always save UTC Time on Database
  }
}
