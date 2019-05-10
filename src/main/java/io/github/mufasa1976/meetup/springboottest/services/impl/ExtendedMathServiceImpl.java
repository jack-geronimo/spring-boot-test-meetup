package io.github.mufasa1976.meetup.springboottest.services.impl;

import io.github.mufasa1976.meetup.springboottest.services.ExtendedMathService;
import io.github.mufasa1976.meetup.springboottest.services.MathService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExtendedMathServiceImpl implements ExtendedMathService {
  private final MathService mathService;

  @Override
  public int divide(int dividend, int divisor) {
    try {
      return mathService.divide(dividend, divisor);
    } catch (ArithmeticException e) {
      throw new IllegalArgumentException(e.getMessage(), e);
    }
  }
}
