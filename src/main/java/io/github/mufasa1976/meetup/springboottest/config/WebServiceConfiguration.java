package io.github.mufasa1976.meetup.springboottest.config;

import io.github.mufasa1976.meetup.springboottest.representation.ws.StarWarsEndpoint;
import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.xml.ws.Endpoint;

@Configuration
public class WebServiceConfiguration {
  @Bean
  public Endpoint starWarsEndpointPublisher(
      Bus bus,
      StarWarsEndpoint starWarsEndpoint,
      @Value("${web-service.star-wars-service:/StarWarsService}") String publishUrl) {
    EndpointImpl endpoint = new EndpointImpl(bus, starWarsEndpoint);
    endpoint.publish(publishUrl);
    return endpoint;
  }
}
