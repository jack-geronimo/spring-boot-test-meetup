package io.github.mufasa1976.meetup.springboottest.services;

import io.github.mufasa1976.meetup.springboottest.services.impl.CalculatorServiceImpl;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = CalculatorServiceImpl.class)
public class CalculatorServiceIntegrationTestWithoutSpringBootTest extends AbstractCalculatorServiceIntegrationTest {}
