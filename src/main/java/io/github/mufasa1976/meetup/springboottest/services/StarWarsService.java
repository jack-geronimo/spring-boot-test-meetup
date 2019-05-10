package io.github.mufasa1976.meetup.springboottest.services;

import io.github.mufasa1976.meetup.springboottest.domains.StarWarsPerson;

import javax.validation.constraints.NotEmpty;
import java.util.Optional;

public interface StarWarsService {
  Optional<StarWarsPerson> getPerson(@NotEmpty String id);
}
