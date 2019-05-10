package io.github.mufasa1976.meetup.springboottest.services;

import com.dneonline.calculator.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

abstract class AbstractCalculatorServiceUnitTest {
  private static final ObjectFactory OBJECT_FACTORY = new ObjectFactory();

  protected abstract CalculatorService getService();

  @Mock
  private WebServiceTemplate webServiceTemplate;

  @Before
  public void init() {
    ((WebServiceGatewaySupport) getService()).setWebServiceTemplate(webServiceTemplate);
  }

  @Test
  public void add_OK() {
    final AddResponse response = OBJECT_FACTORY.createAddResponse();
    response.setAddResult(3);

    // Note: can't use dedicated Request-Class because JAXB wont generate equals/hashCode without any Plugins --> use any(Add.class)
    when(webServiceTemplate.marshalSendAndReceive(any(Add.class), any(WebServiceMessageCallback.class))).thenReturn(response);

    assertThat(getService().add(1, 2)).isEqualTo(response.getAddResult());
    verify(webServiceTemplate).marshalSendAndReceive(any(Add.class), any(WebServiceMessageCallback.class));
  }

  @Test(expected = ArithmeticException.class)
  public void add_NOK_arithmeticException() {
    when(webServiceTemplate.marshalSendAndReceive(any(Add.class), any(WebServiceMessageCallback.class))).thenThrow(new ArithmeticException());
    try {
      getService().add(1, 2);
    } finally {
      verify(webServiceTemplate).marshalSendAndReceive(any(Add.class), any(WebServiceMessageCallback.class));
    }
  }

  @Test
  public void subtract_OK() {
    final SubtractResponse response = OBJECT_FACTORY.createSubtractResponse();
    response.setSubtractResult(-1);

    // Note: can't use dedicated Request-Class because JAXB wont generate equals/hashCode without any Plugins --> use any(Add.class)
    when(webServiceTemplate.marshalSendAndReceive(any(Subtract.class), any(WebServiceMessageCallback.class))).thenReturn(response);

    assertThat(getService().subtract(1, 2)).isEqualTo(response.getSubtractResult());
    verify(webServiceTemplate).marshalSendAndReceive(any(Subtract.class), any(WebServiceMessageCallback.class));
  }

  @Test(expected = ArithmeticException.class)
  public void subtract_NOK_arithmeticException() {
    when(webServiceTemplate.marshalSendAndReceive(any(Subtract.class), any(WebServiceMessageCallback.class))).thenThrow(new ArithmeticException());
    try {
      getService().subtract(1, 2);
    } finally {
      verify(webServiceTemplate).marshalSendAndReceive(any(Subtract.class), any(WebServiceMessageCallback.class));
    }
  }

  @Test
  public void multiply_OK() {
    final MultiplyResponse response = OBJECT_FACTORY.createMultiplyResponse();
    response.setMultiplyResult(2);

    // Note: can't use dedicated Request-Class because JAXB wont generate equals/hashCode without any Plugins --> use any(Add.class)
    when(webServiceTemplate.marshalSendAndReceive(any(Multiply.class), any(WebServiceMessageCallback.class))).thenReturn(response);

    assertThat(getService().multiply(1, 2)).isEqualTo(response.getMultiplyResult());
    verify(webServiceTemplate).marshalSendAndReceive(any(Multiply.class), any(WebServiceMessageCallback.class));
  }

  @Test(expected = ArithmeticException.class)
  public void multiply_NOK_arithmeticException() {
    when(webServiceTemplate.marshalSendAndReceive(any(Multiply.class), any(WebServiceMessageCallback.class))).thenThrow(new ArithmeticException());
    try {
      getService().multiply(1, 2);
    } finally {
      verify(webServiceTemplate).marshalSendAndReceive(any(Multiply.class), any(WebServiceMessageCallback.class));
    }
  }

  @Test
  public void divide_OK() {
    final DivideResponse response = OBJECT_FACTORY.createDivideResponse();
    response.setDivideResult(0);

    // Note: can't use dedicated Request-Class because JAXB wont generate equals/hashCode without any Plugins --> use any(Add.class)
    when(webServiceTemplate.marshalSendAndReceive(any(Divide.class), any(WebServiceMessageCallback.class))).thenReturn(response);

    assertThat(getService().divide(1, 2)).isEqualTo(response.getDivideResult());
    verify(webServiceTemplate).marshalSendAndReceive(any(Divide.class), any(WebServiceMessageCallback.class));
  }

  @Test(expected = ArithmeticException.class)
  public void divice_NOK_arithmeticException() {
    when(webServiceTemplate.marshalSendAndReceive(any(Divide.class), any(WebServiceMessageCallback.class))).thenThrow(new ArithmeticException());
    try {
      getService().divide(1, 2);
    } finally {
      verify(webServiceTemplate).marshalSendAndReceive(any(Divide.class), any(WebServiceMessageCallback.class));
    }
  }
}
