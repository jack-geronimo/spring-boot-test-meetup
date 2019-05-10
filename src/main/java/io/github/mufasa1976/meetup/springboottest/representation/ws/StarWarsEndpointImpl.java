package io.github.mufasa1976.meetup.springboottest.representation.ws;

import io.github.mufasa1976.meetup.springboottest.domains.StarWarsPerson;
import io.github.mufasa1976.meetup.springboottest.services.StarWarsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jws.WebService;

@Component
@WebService(endpointInterface = "io.github.mufasa1976.meetup.springboottest.representation.ws.StarWarsEndpoint", portName = "StarWarsEndpointPort")
public class StarWarsEndpointImpl implements StarWarsEndpoint {
  @Autowired
  private StarWarsService starWarsService;

  @Override
  public StarWarsPerson getPerson(String id) {
    return starWarsService.getPerson(id)
                          .orElse(null);
  }
}
