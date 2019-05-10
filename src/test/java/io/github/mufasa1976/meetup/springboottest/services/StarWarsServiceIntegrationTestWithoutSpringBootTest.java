package io.github.mufasa1976.meetup.springboottest.services;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ServerList;
import io.github.mufasa1976.meetup.springboottest.config.FeignConfiguration;
import io.github.mufasa1976.meetup.springboottest.services.impl.StarWarsServiceImpl;
import org.junit.ClassRule;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cloud.contract.wiremock.WireMockSpring;
import org.springframework.cloud.netflix.ribbon.StaticServerList;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = StarWarsServiceIntegrationTestWithoutSpringBootTest.Configuration.class)
public class StarWarsServiceIntegrationTestWithoutSpringBootTest extends AbstractStarWarsServiceIntegrationTest {
  @ClassRule
  public static WireMockRule WIREMOCK = new WireMockRule(WireMockSpring.options().dynamicHttpsPort());

  @TestConfiguration
  @Import(FeignConfiguration.class)
  @ComponentScan(basePackageClasses = StarWarsService.class, useDefaultFilters = false, includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = StarWarsServiceImpl.class))
  @EnableAutoConfiguration
  public static class Configuration {
    @Bean
    public ServerList<Server> ribbonServerList() {
      return new StaticServerList<>(new Server("localhost", WIREMOCK.port()));
    }
  }
}
