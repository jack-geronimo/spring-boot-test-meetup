package io.github.mufasa1976.meetup.springboottest.services;

import io.github.mufasa1976.meetup.springboottest.services.impl.CalculatorServiceImpl;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CalculatorServiceImpl.class)
public class CalculatorServiceIntegrationTestWithSpringBootTest extends AbstractCalculatorServiceIntegrationTest {}
