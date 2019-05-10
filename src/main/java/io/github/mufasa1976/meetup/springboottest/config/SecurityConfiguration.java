package io.github.mufasa1976.meetup.springboottest.config;

import io.github.mufasa1976.meetup.springboottest.Routes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  @Value("${cxf.path}")
  private String webServicePath;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests()
        .antMatchers(Routes.API + "/**", webServicePath + "/**").authenticated()
        .anyRequest().permitAll()
        .and()
        .httpBasic()
        .and()
        .csrf().ignoringAntMatchers(webServicePath + "/**");
  }
}
