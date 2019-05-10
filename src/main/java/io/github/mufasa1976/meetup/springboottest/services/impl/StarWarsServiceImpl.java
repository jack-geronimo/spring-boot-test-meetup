package io.github.mufasa1976.meetup.springboottest.services.impl;

import io.github.mufasa1976.meetup.springboottest.cients.StarWarsClient;
import io.github.mufasa1976.meetup.springboottest.domains.starwars.Person;
import io.github.mufasa1976.meetup.springboottest.services.StarWarsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotEmpty;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StarWarsServiceImpl implements StarWarsService {
  private final StarWarsClient starWarsClient;

  @Override
  public Optional<Person> getPerson(@NotEmpty String id) {
    return starWarsClient.getPerson(id);
  }
}
