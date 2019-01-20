package io.github.mufasa1976.meetup.springboottest.services;

import io.github.mufasa1976.meetup.springboottest.domains.PersonalEvent;
import io.github.mufasa1976.meetup.springboottest.queries.PersonalEventQuery;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedResources;

import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.UUID;

public interface PersonalEventService {
  PagedResources<PersonalEvent> findAll(PersonalEventQuery query, Pageable pageable);

  Optional<PersonalEvent> getOne(@NotNull UUID reference);

  void create(@NotNull PersonalEvent personalEvent);

  void update(@NotNull UUID reference, @NotNull PersonalEvent personalEvent);

  void delete(@NotNull UUID reference);
}
