package io.github.mufasa1976.meetup.springboottest.representation.rest;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ServerList;
import org.junit.ClassRule;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cloud.contract.wiremock.WireMockSpring;
import org.springframework.cloud.netflix.ribbon.StaticServerList;
import org.springframework.context.annotation.Bean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
public class StarWarsRestControllerIntegrationTestWithSpringBootTest extends AbstractStarWarsRestControllerIntegrationTest {
  @ClassRule
  public static WireMockRule WIREMOCK = new WireMockRule(WireMockSpring.options().dynamicHttpsPort());

  @TestConfiguration
  public static class Configuration {
    @Bean
    public ServerList<Server> ribbonServerList() {
      return new StaticServerList<>(new Server("localhost", WIREMOCK.port()));
    }
  }
}
