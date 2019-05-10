package io.github.mufasa1976.meetup.springboottest.services;

import io.github.mufasa1976.meetup.springboottest.services.impl.ExtendedMathServiceImpl;
import io.github.mufasa1976.meetup.springboottest.services.impl.MathServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { MathServiceImpl.class, ExtendedMathServiceImpl.class })
public class ExtendedMathServiceTestWithSpyBean {
  @SpyBean
  private MathService mathService;

  @Autowired
  private ExtendedMathService extendedMathService;

  @Test
  public void divide_OK() {
    assertThat(extendedMathService.divide(4, 2)).isEqualTo(2);
    verify(mathService, times(1)).divide(anyInt(), anyInt());
  }

  @Test(expected = IllegalArgumentException.class)
  public void divide_NOK() {
    try {
      extendedMathService.divide(1, 0);
    } finally {
      verify(mathService).divide(1, 0);
    }
  }

  @Test
  public void divide_OK_mockedDivideByZero() {
    doReturn(Integer.MAX_VALUE).when(mathService).divide(1, 0);
    assertThat(extendedMathService.divide(1, 0)).isEqualTo(Integer.MAX_VALUE);
  }
}
