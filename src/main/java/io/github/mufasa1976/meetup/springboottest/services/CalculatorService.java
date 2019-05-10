package io.github.mufasa1976.meetup.springboottest.services;

public interface CalculatorService {
  int add(int augend, int addend);

  int subtract(int minuend, int subtrahend);

  int multiply(int multiplier, int multiplicand);

  int divide(int dividend, int divisor);
}
