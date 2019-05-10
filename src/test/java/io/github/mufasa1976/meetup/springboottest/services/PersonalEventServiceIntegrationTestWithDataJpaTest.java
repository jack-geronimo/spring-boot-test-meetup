package io.github.mufasa1976.meetup.springboottest.services;

import io.github.mufasa1976.meetup.springboottest.assembler.PersonalEventResourceAssembler;
import io.github.mufasa1976.meetup.springboottest.config.DatabaseConfiguration;
import io.github.mufasa1976.meetup.springboottest.services.impl.PersonalEventServiceImpl;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@RunWith(SpringRunner.class)
@DataJpaTest
@ContextConfiguration(classes = {PersonalEventServiceIntegrationTestWithDataJpaTest.Configuration.class, PersonalEventServiceImpl.class, PersonalEventResourceAssembler.class})
@TestPropertySource(properties = "spring.jpa.show-sql=true")
public class PersonalEventServiceIntegrationTestWithDataJpaTest extends AbstractPersonalEventServiceIntegrationTest {
  @TestConfiguration
  @Import(DatabaseConfiguration.class)
  @EnableSpringDataWebSupport
  @EnableAutoConfiguration
  public static class Configuration {}

  @Before
  public void setUp() {
    HttpServletRequest request = new MockHttpServletRequest();
    RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request)); // needed by PagedResourcesAssembler
  }
}
