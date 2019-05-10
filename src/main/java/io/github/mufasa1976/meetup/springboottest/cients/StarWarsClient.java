package io.github.mufasa1976.meetup.springboottest.cients;

import io.github.mufasa1976.meetup.springboottest.domains.StarWarsPerson;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(name = "star-wars", decode404 = true, configuration = StarWarsClient.Configuration.class)
public interface StarWarsClient {
  class Configuration {}

  @GetMapping("/api/people/{id}/")
  Optional<StarWarsPerson> getPerson(@PathVariable("id") String id);
}
