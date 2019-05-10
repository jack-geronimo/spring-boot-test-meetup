package io.github.mufasa1976.meetup.springboottest.representation.rest;

import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;
import io.github.mufasa1976.meetup.springboottest.Routes;
import io.github.mufasa1976.meetup.springboottest.services.CalculatorService;
import io.github.mufasa1976.meetup.springboottest.services.impl.CalculatorServiceImpl;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.NestedServletException;
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
import static org.hamcrest.Matchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.ws.test.client.RequestMatchers.connectionTo;
import static org.springframework.ws.test.client.ResponseCreators.withServerOrReceiverFault;

@AutoConfigureMockMvc
abstract class AbstractCalculatorRestControllerIntegrationTest {
  @Data
  @AllArgsConstructor
  @Builder
  private static class MathematicalOperation {
    private int a;
    private int b;
  }

  private static final String FAULT_MESSAGE = "something has been going wrong";

  @Autowired
  private MockMvc web;

  private MockWebServiceServer backend;

  @Autowired
  private ResourceLoader resourceLoader;

  @Autowired
  private CalculatorService calculatorService;

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

    web.perform(get(Routes.CALCULATOR_ADD, "1", "2"))
       .andExpect(status().isOk())
       .andExpect(content().contentType(APPLICATION_JSON_UTF8))
       .andExpect(jsonPath("$").value(is(3)));
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
      web.perform(get(Routes.CALCULATOR_ADD, "1", "2"));
      fail("%s should've been thrown", ArithmeticException.class);
    } catch (NestedServletException e) {
      assertThat(e.getCause()).isInstanceOfSatisfying(ArithmeticException.class,
          arithmeticException -> assertThat(arithmeticException).hasMessage(FAULT_MESSAGE));
    }
  }

  @Test
  public void subtract_OK() throws Exception {
    backend.expect(connectionTo(CalculatorServiceImpl.WEBSERVICE_LOCATION))
           .andExpect(soapAction(CalculatorServiceImpl.SUBTRACT_ACTION))
           .andExpect(payload("Subtract", new MathematicalOperation(1, 2)))
           .andRespond(withPayload("SubtractResponse", -1));

    web.perform(get(Routes.CALCULATOR_SUBTRACT, "1", "2"))
       .andExpect(status().isOk())
       .andExpect(content().contentType(APPLICATION_JSON_UTF8))
       .andExpect(jsonPath("$").value(is(-1)));
  }

  @Test
  public void subtract_NOK_faultFromBackend() throws Exception {
    backend.expect(connectionTo(CalculatorServiceImpl.WEBSERVICE_LOCATION))
           .andExpect(soapAction(CalculatorServiceImpl.SUBTRACT_ACTION))
           .andExpect(payload("Subtract", new MathematicalOperation(1, 2)))
           .andRespond(withServerOrReceiverFault(FAULT_MESSAGE, Locale.getDefault()));

    try {
      web.perform(get(Routes.CALCULATOR_SUBTRACT, "1", "2"));
      fail("%s should've been thrown", ArithmeticException.class);
    } catch (NestedServletException e) {
      assertThat(e.getCause()).isInstanceOfSatisfying(ArithmeticException.class,
          arithmeticException -> assertThat(arithmeticException).hasMessage(FAULT_MESSAGE));
    }
  }

  @Test
  public void multiply_OK() throws Exception {
    backend.expect(connectionTo(CalculatorServiceImpl.WEBSERVICE_LOCATION))
           .andExpect(soapAction(CalculatorServiceImpl.MULTIPLY_ACTION))
           .andExpect(payload("Multiply", new MathematicalOperation(1, 2)))
           .andRespond(withPayload("MultiplyResponse", 2));

    web.perform(get(Routes.CALCULATOR_MULTIPLY, "1", "2"))
       .andExpect(status().isOk())
       .andExpect(content().contentType(APPLICATION_JSON_UTF8))
       .andExpect(jsonPath("$").value(is(2)));
  }

  @Test
  public void multiply_NOK_faultFromBackend() throws Exception {
    backend.expect(connectionTo(CalculatorServiceImpl.WEBSERVICE_LOCATION))
           .andExpect(soapAction(CalculatorServiceImpl.MULTIPLY_ACTION))
           .andExpect(payload("Multiply", new MathematicalOperation(1, 2)))
           .andRespond(withServerOrReceiverFault(FAULT_MESSAGE, Locale.getDefault()));

    try {
      web.perform(get(Routes.CALCULATOR_MULTIPLY, "1", "2"));
      fail("%s should've been thrown", ArithmeticException.class);
    } catch (NestedServletException e) {
      assertThat(e.getCause()).isInstanceOfSatisfying(ArithmeticException.class,
          arithmeticException -> assertThat(arithmeticException).hasMessage(FAULT_MESSAGE));
    }
  }

  @Test
  public void divide_OK() throws Exception {
    backend.expect(connectionTo(CalculatorServiceImpl.WEBSERVICE_LOCATION))
           .andExpect(soapAction(CalculatorServiceImpl.DIVIDE_ACTION))
           .andExpect(payload("Divide", new MathematicalOperation(1, 2)))
           .andRespond(withPayload("DivideResponse", 0));

    web.perform(get(Routes.CALCULATOR_DIVIDE, "1", "2"))
       .andExpect(status().isOk())
       .andExpect(content().contentType(APPLICATION_JSON_UTF8))
       .andExpect(jsonPath("$").value(is(0)));
  }

  @Test
  public void divide_NOK_faultFromBackend() throws Exception {
    backend.expect(connectionTo(CalculatorServiceImpl.WEBSERVICE_LOCATION))
           .andExpect(soapAction(CalculatorServiceImpl.DIVIDE_ACTION))
           .andExpect(payload("Divide", new MathematicalOperation(1, 2)))
           .andRespond(withServerOrReceiverFault(FAULT_MESSAGE, Locale.getDefault()));

    try {
      web.perform(get(Routes.CALCULATOR_DIVIDE, "1", "2"));
      fail("%s should've been thrown", ArithmeticException.class);
    } catch (NestedServletException e) {
      assertThat(e.getCause()).isInstanceOfSatisfying(ArithmeticException.class,
          arithmeticException -> assertThat(arithmeticException).hasMessage(FAULT_MESSAGE));
    }
  }
}
