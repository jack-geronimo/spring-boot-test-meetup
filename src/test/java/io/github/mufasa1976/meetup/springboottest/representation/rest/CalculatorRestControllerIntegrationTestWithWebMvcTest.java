package io.github.mufasa1976.meetup.springboottest.representation.rest;

import io.github.mufasa1976.meetup.springboottest.config.SecurityConfiguration;
import io.github.mufasa1976.meetup.springboottest.services.impl.CalculatorServiceImpl;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@WebMvcTest(CalculatorRestController.class)
@ContextConfiguration(classes = { CalculatorRestController.class, CalculatorServiceImpl.class })
@Import(SecurityConfiguration.class)
@WithMockUser
public class CalculatorRestControllerIntegrationTestWithWebMvcTest extends AbstractCalculatorRestControllerIntegrationTest {}
