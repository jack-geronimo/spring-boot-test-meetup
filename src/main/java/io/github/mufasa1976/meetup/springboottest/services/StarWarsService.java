package io.github.mufasa1976.meetup.springboottest.services;

import io.github.mufasa1976.meetup.springboottest.domains.starwars.Person;

import javax.validation.constraints.NotEmpty;
import java.util.Optional;

public interface StarWarsService {
  Optional<Person> getPerson(@NotEmpty String id);
}
