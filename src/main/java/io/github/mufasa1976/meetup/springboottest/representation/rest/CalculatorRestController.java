package io.github.mufasa1976.meetup.springboottest.representation.rest;

import io.github.mufasa1976.meetup.springboottest.Routes;
import io.github.mufasa1976.meetup.springboottest.services.CalculatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated
public class CalculatorRestController {
  private final CalculatorService calculatorService;

  @GetMapping(Routes.CALCULATOR_ADD)
  public int add(@PathVariable(Routes.Param.AUGEND) int augend, @PathVariable(Routes.Param.ADDEND) int addend) {
    return calculatorService.add(augend, addend);
  }

  @GetMapping(Routes.CALCULATOR_SUBTRACT)
  public int subtract(@PathVariable(Routes.Param.MINUEND) int minuend, @PathVariable(Routes.Param.SUBTRAHEND) int subtrahend) {
    return calculatorService.subtract(minuend, subtrahend);
  }

  @GetMapping(Routes.CALCULATOR_MULTIPLY)
  public int multiply(@PathVariable(Routes.Param.MULTIPLIER) int mulitplier, @PathVariable(Routes.Param.MULTIPLICAND) int multiplicand) {
    return calculatorService.multiply(mulitplier, multiplicand);
  }

  @GetMapping(Routes.CALCULATOR_DIVIDE)
  public int divide(@PathVariable(Routes.Param.DIVIDEND) int dividend, @PathVariable(Routes.Param.DIVISOR) int divisor) {
    return calculatorService.divide(dividend, divisor);
  }
}
