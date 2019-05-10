package io.github.mufasa1976.meetup.springboottest.representation.rest;

import io.github.mufasa1976.meetup.springboottest.config.SecurityConfiguration;
import io.github.mufasa1976.meetup.springboottest.services.impl.CalculatorServiceImpl;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { CalculatorRestController.class, CalculatorServiceImpl.class })
@EnableAutoConfiguration
@Import(SecurityConfiguration.class)
@WithMockUser
public class CalculatorRestControllerIntegrationTestWithSpringBootTest extends AbstractCalculatorRestControllerIntegrationTest {}
