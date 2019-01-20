package io.github.mufasa1976.meetup.springboottest.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MathServiceTestWithSpringBootTest {
  @Autowired
  private MathService mathService;

  @Test
  public void divide_OK() {
    assertThat(mathService.divide(4, 2)).isEqualTo(2);
  }

  @Test(expected = ArithmeticException.class)
  public void divide_NOK_divideByZero_throwArithmeticException() {
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
