package io.github.mufasa1976.meetup.springboottest.services;

import io.github.mufasa1976.meetup.springboottest.services.impl.CalculatorServiceImpl;
import lombok.Getter;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CalculatorServiceUnitTestWithoutSpring extends AbstractCalculatorServiceUnitTest {
  @Getter
  private CalculatorService service;

  @Before
  @Override
  public void init() {
    this.service = new CalculatorServiceImpl();
    super.init();
  }
}
