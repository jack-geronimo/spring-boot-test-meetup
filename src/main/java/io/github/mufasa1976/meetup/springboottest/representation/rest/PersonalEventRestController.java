package io.github.mufasa1976.meetup.springboottest.representation.rest;

import io.github.mufasa1976.meetup.springboottest.Routes;
import io.github.mufasa1976.meetup.springboottest.domains.PersonalEvent;
import io.github.mufasa1976.meetup.springboottest.queries.PersonalEventQuery;
import io.github.mufasa1976.meetup.springboottest.services.PersonalEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

import static io.github.mufasa1976.meetup.springboottest.Routes.PERSONAL_EVENT;
import static io.github.mufasa1976.meetup.springboottest.Routes.PERSONAL_EVENTS;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequiredArgsConstructor
@Validated
public class PersonalEventRestController {
  private final PersonalEventService service;

  @GetMapping(PERSONAL_EVENTS)
  public PagedResources<PersonalEvent> findAll(@ModelAttribute PersonalEventQuery query, Pageable pageable) {
    return service.findAll(query, pageable);
  }

  @GetMapping(PERSONAL_EVENT)
  public ResponseEntity<PersonalEvent> getOne(@PathVariable("reference") UUID reference) {
    return service.getOne(reference)
                  .map(ResponseEntity::ok)
                  .orElseGet(ResponseEntity.notFound()::build);
  }

  @PostMapping(PERSONAL_EVENTS)
  @ResponseStatus(CREATED)
  public void create(@RequestBody @Valid PersonalEvent personalEvent) {
    service.create(personalEvent);
  }

  @PutMapping(PERSONAL_EVENT)
  @ResponseStatus(NO_CONTENT)
  public void update(@PathVariable("reference") UUID reference, @RequestBody @Valid PersonalEvent personalEvent) {
    service.update(reference, personalEvent);
  }

  @DeleteMapping(PERSONAL_EVENT)
  @ResponseStatus(NO_CONTENT)
  public void delete(@PathVariable("reference") UUID reference) {
    service.delete(reference);
  }
}
