package io.github.mufasa1976.meetup.springboottest.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = ExtendedMathServiceImpl.class)
public class ExtendedMathServiceTestWithSpring {
  @MockBean
  private MathService mathService;

  @Autowired
  private ExtendedMathService extendedMathService;

  @Test
  public void divide_OK() {
    doReturn(2).when(mathService).divide(4, 2);
    assertThat(extendedMathService.divide(4, 2)).isEqualTo(2);
  }

  @Test(expected = IllegalArgumentException.class)
  public void divide_NOK_divisionByZero() {
    doThrow(ArithmeticException.class).when(mathService).divide(1, 0);
    extendedMathService.divide(1, 0);
  }

  @Test
  public void divide_NOK_divideByZero_catchIllegalArgumentException() {
    doThrow(new ArithmeticException("/ by zero")).when(mathService).divide(1, 0);
    try {
      extendedMathService.divide(1, 0);
      fail("%s should be thrown", IllegalArgumentException.class);
    } catch (IllegalArgumentException e) {
      assertThat(e).hasMessage("/ by zero")
                   .hasCauseInstanceOf(ArithmeticException.class);
    }
  }

  @Test
  public void divide_NOK_divideByZero_catchException() {
    doThrow(new ArithmeticException("/ by zero")).when(mathService).divide(1, 0);
    try {
      extendedMathService.divide(1, 0);
      fail("%s should be thrown", IllegalArgumentException.class);
    } catch (Exception e) {
      assertThat(e).isInstanceOfSatisfying(IllegalArgumentException.class, illegalArgumentException -> {
        assertThat(illegalArgumentException).hasMessage("/ by zero")
                                            .hasCauseInstanceOf(ArithmeticException.class);
      });
    }
  }
}
