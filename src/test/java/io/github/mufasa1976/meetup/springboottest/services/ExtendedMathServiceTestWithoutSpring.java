package io.github.mufasa1976.meetup.springboottest.services;

import io.github.mufasa1976.meetup.springboottest.services.impl.ExtendedMathServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@RunWith(MockitoJUnitRunner.class)
public class ExtendedMathServiceTestWithoutSpring {
  @Mock
  private MathService mathService;

  private ExtendedMathService extendedMathService;

  @Before
  public void setUp() {
    extendedMathService = new ExtendedMathServiceImpl(mathService);
  }

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
