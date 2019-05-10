package io.github.mufasa1976.meetup.springboottest.services;

import io.github.mufasa1976.meetup.springboottest.services.impl.CalculatorServiceImpl;
import lombok.Getter;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = CalculatorServiceImpl.class)
public class CalculatorServiceUnitTestWithSpring extends AbstractCalculatorServiceUnitTest {
  @Getter
  @Autowired
  private CalculatorService service;
}
