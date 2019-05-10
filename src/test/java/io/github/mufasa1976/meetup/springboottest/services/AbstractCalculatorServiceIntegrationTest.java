package io.github.mufasa1976.meetup.springboottest.services;

import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;
import io.github.mufasa1976.meetup.springboottest.services.impl.CalculatorServiceImpl;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.ws.test.client.*;
import org.springframework.xml.transform.StringSource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.springframework.ws.test.client.RequestMatchers.connectionTo;
import static org.springframework.ws.test.client.ResponseCreators.withServerOrReceiverFault;

abstract class AbstractCalculatorServiceIntegrationTest {
  @Data
  @AllArgsConstructor
  @Builder
  private static class MathematicalOperation {
    private int a;
    private int b;
  }

  private static final String FAULT_MESSAGE = "something has been going wrong";

  @Autowired
  private CalculatorService calculatorService;

  @Autowired
  private ResourceLoader resourceLoader;

  private MockWebServiceServer backend;

  @Before
  public void setUp() {
    backend = MockWebServiceServer.createServer(((WebServiceGatewaySupport) calculatorService).getWebServiceTemplate());
  }

  @After
  public void tearDown() {
    backend.verify();
  }

  @Test
  public void add_OK() throws Exception {
    backend.expect(connectionTo(CalculatorServiceImpl.WEBSERVICE_LOCATION))
           .andExpect(soapAction(CalculatorServiceImpl.ADD_ACTION))
           .andExpect(payload("Add", new MathematicalOperation(1, 2)))
           .andRespond(withPayload("AddResponse", 3));

    assertThat(calculatorService.add(1, 2)).isEqualTo(3);
  }

  protected RequestMatcher soapAction(String soapAction) {
    return (uri, request) -> assertThat(request).isInstanceOfSatisfying(SoapMessage.class,
        soapMessage -> assertThat(soapMessage).as("Wrong SOAP Action")
                                              .extracting(SoapMessage::getSoapAction)
                                              .isEqualTo(String.format("\"%s\"", soapAction)));
  }

  protected RequestMatcher payload(String templateName, Object context) throws IOException {
    return RequestMatchers.payload(new StringSource(getTemplate("requests", templateName).execute(context)));
  }

  private Template getTemplate(String directory, String name) throws IOException {
    Resource templateResource = resourceLoader.getResource(String.format("classpath:/ws/calculatorService/%s/%s.mustache", directory, name));
    try (InputStream inputStream = templateResource.getInputStream()) {
      return Mustache.compiler().compile(IOUtils.toString(inputStream, StandardCharsets.UTF_8));
    }
  }

  protected ResponseCreator withPayload(String templateName, Object context) throws IOException {
    return ResponseCreators.withPayload(new StringSource(getTemplate("responses", templateName).execute(context)));
  }

  @Test
  public void add_NOK_faultFromBackend() throws Exception {
    backend.expect(connectionTo(CalculatorServiceImpl.WEBSERVICE_LOCATION))
           .andExpect(soapAction(CalculatorServiceImpl.ADD_ACTION))
           .andExpect(payload("Add", new MathematicalOperation(1, 2)))
           .andRespond(withServerOrReceiverFault(FAULT_MESSAGE, Locale.getDefault()));

    try {
      calculatorService.add(1, 2);
      fail("%s should've been thrown", ArithmeticException.class);
    } catch (ArithmeticException e) {
      assertThat(e).hasMessage(FAULT_MESSAGE);
    }
  }

  @Test
  public void subtract_OK() throws Exception {
    backend.expect(connectionTo(CalculatorServiceImpl.WEBSERVICE_LOCATION))
           .andExpect(soapAction(CalculatorServiceImpl.SUBTRACT_ACTION))
           .andExpect(payload("Subtract", new MathematicalOperation(1, 2)))
           .andRespond(withPayload("SubtractResponse", -1));

    assertThat(calculatorService.subtract(1, 2)).isEqualTo(-1);
  }

  @Test
  public void subtract_NOK_faultFromBackend() throws Exception {
    backend.expect(connectionTo(CalculatorServiceImpl.WEBSERVICE_LOCATION))
           .andExpect(soapAction(CalculatorServiceImpl.SUBTRACT_ACTION))
           .andExpect(payload("Subtract", new MathematicalOperation(1, 2)))
           .andRespond(withServerOrReceiverFault(FAULT_MESSAGE, Locale.getDefault()));

    try {
      calculatorService.subtract(1, 2);
      fail("%s should've been thrown", ArithmeticException.class);
    } catch (ArithmeticException e) {
      assertThat(e).hasMessage(FAULT_MESSAGE);
    }
  }

  @Test
  public void multiply_OK() throws Exception {
    backend.expect(connectionTo(CalculatorServiceImpl.WEBSERVICE_LOCATION))
           .andExpect(soapAction(CalculatorServiceImpl.MULTIPLY_ACTION))
           .andExpect(payload("Multiply", new MathematicalOperation(1, 2)))
           .andRespond(withPayload("MultiplyResponse", 2));

    assertThat(calculatorService.multiply(1, 2)).isEqualTo(2);
  }

  @Test
  public void multiply_NOK_faultFromBackend() throws Exception {
    backend.expect(connectionTo(CalculatorServiceImpl.WEBSERVICE_LOCATION))
           .andExpect(soapAction(CalculatorServiceImpl.MULTIPLY_ACTION))
           .andExpect(payload("Multiply", new MathematicalOperation(1, 2)))
           .andRespond(withServerOrReceiverFault(FAULT_MESSAGE, Locale.getDefault()));

    try {
      calculatorService.multiply(1, 2);
      fail("%s should've been thrown", ArithmeticException.class);
    } catch (ArithmeticException e) {
      assertThat(e).hasMessage(FAULT_MESSAGE);
    }
  }

  @Test
  public void divide_OK() throws Exception {
    backend.expect(connectionTo(CalculatorServiceImpl.WEBSERVICE_LOCATION))
           .andExpect(soapAction(CalculatorServiceImpl.DIVIDE_ACTION))
           .andExpect(payload("Divide", new MathematicalOperation(1, 2)))
           .andRespond(withPayload("DivideResponse", 0));

    assertThat(calculatorService.divide(1, 2)).isEqualTo(0);
  }

  @Test
  public void divide_NOK_faultFromBackend() throws Exception {
    backend.expect(connectionTo(CalculatorServiceImpl.WEBSERVICE_LOCATION))
           .andExpect(soapAction(CalculatorServiceImpl.DIVIDE_ACTION))
           .andExpect(payload("Divide", new MathematicalOperation(1, 2)))
           .andRespond(withServerOrReceiverFault(FAULT_MESSAGE, Locale.getDefault()));

    try {
      calculatorService.divide(1, 2);
      fail("%s should've been thrown", ArithmeticException.class);
    } catch (ArithmeticException e) {
      assertThat(e).hasMessage(FAULT_MESSAGE);
    }
  }
}
