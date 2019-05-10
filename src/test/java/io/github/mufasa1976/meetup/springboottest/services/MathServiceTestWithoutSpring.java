package io.github.mufasa1976.meetup.springboottest.services;

import io.github.mufasa1976.meetup.springboottest.services.impl.MathServiceImpl;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class MathServiceTestWithoutSpring {
  private MathService mathService;

  @Before
  public void setUp() {
    mathService = new MathServiceImpl();
  }

  @Test
  public void divide_OK() {
    assertThat(mathService.divide(4, 2)).isEqualTo(2);
  }

  @Test(expected = ArithmeticException.class)
  public void divide_NOK_divideByZero() {
    mathService.divide(1, 0);
  }

  @Test
  public void divide_NOK_divideByZero_catchArithmeticException() {
    try {
      mathService.divide(1, 0);
      fail("%s should be thrown", ArithmeticException.class); // !! IMPORTANT !!
    } catch (ArithmeticException e) {
      assertThat(e).hasMessage("/ by zero");
    }
  }

  @Test
  public void divide_NOK_divideByZero_catchException() {
    try {
      mathService.divide(1, 0);
      fail("%s should be thrown", ArithmeticException.class); // !! IMPORTANT !!
    } catch (Exception e) {
      assertThat(e).isInstanceOfSatisfying(ArithmeticException.class, arithmeticException -> {
        assertThat(arithmeticException).hasMessage("/ by zero");
      });
    }
  }
}
