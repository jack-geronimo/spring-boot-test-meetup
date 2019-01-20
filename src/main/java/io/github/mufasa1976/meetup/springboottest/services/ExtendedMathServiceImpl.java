package io.github.mufasa1976.meetup.springboottest.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExtendedMathServiceImpl implements ExtendedMathService {
  private final MathService mathService;

  @Override
  public int divide(int divisor, int divident) {
    try {
      return mathService.divide(divisor, divident);
    } catch (ArithmeticException e) {
      throw new IllegalArgumentException(e.getMessage(), e);
    }
  }
}
