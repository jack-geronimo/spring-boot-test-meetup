package io.github.mufasa1976.meetup.springboottest.representation.rest;

import io.github.mufasa1976.meetup.springboottest.Routes;
import io.github.mufasa1976.meetup.springboottest.domains.StarWarsPerson;
import io.github.mufasa1976.meetup.springboottest.services.StarWarsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated
public class StarWarsRestController {
  private final StarWarsService starWarsService;

  @GetMapping(Routes.PERSON)
  public ResponseEntity<StarWarsPerson> getPerson(@PathVariable(Routes.Param.ID) String id) {
    return starWarsService.getPerson(id)
                          .map(ResponseEntity::ok)
                          .orElseGet(ResponseEntity.notFound()::build);
  }
}
