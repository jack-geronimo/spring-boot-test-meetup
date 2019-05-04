package io.github.mufasa1976.meetup.springboottest.representation.rest;

import io.github.mufasa1976.meetup.springboottest.assembler.PersonalEventResourceAssembler;
import io.github.mufasa1976.meetup.springboottest.config.DatabaseConfiguration;
import io.github.mufasa1976.meetup.springboottest.config.SecurityConfiguration;
import io.github.mufasa1976.meetup.springboottest.services.PersonalEventService;
import io.github.mufasa1976.meetup.springboottest.services.PersonalEventServiceImpl;
import io.github.mufasa1976.meetup.springboottest.services.StarWarsServiceImpl;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@WebMvcTest(PersonalEventRestController.class)
@ContextConfiguration(classes = PersonalEventRestControllerIntegrationTestWithWebMvcTest.Configuration.class)
@TestPropertySource(properties = "spring.jpa.show-sql=true")
public class PersonalEventRestControllerIntegrationTestWithWebMvcTest extends AbstractPersonalEventRestControllerIntegrationTest {
  @TestConfiguration
  @Import({ DatabaseConfiguration.class, SecurityConfiguration.class })
  @ComponentScan(basePackageClasses = PersonalEventService.class, useDefaultFilters = false, includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = PersonalEventServiceImpl.class))
  @ComponentScan(basePackageClasses = PersonalEventResourceAssembler.class, includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = PersonalEventResourceAssembler.class))
  @EnableAutoConfiguration
  @EnableSpringDataWebSupport
  public static class Configuration {}
}
