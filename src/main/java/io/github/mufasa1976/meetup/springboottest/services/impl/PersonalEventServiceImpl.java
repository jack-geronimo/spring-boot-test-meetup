package io.github.mufasa1976.meetup.springboottest.services.impl;

import io.github.mufasa1976.meetup.springboottest.assembler.PersonalEventResourceAssembler;
import io.github.mufasa1976.meetup.springboottest.domains.PersonalEvent;
import io.github.mufasa1976.meetup.springboottest.entities.PersonalEventEntity;
import io.github.mufasa1976.meetup.springboottest.exceptions.RowNotFoundException;
import io.github.mufasa1976.meetup.springboottest.queries.PersonalEventQuery;
import io.github.mufasa1976.meetup.springboottest.repositories.PersonalEventRepository;
import io.github.mufasa1976.meetup.springboottest.services.PersonalEventService;
import io.github.mufasa1976.meetup.springboottest.utils.WhereClauseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.persistence.EntityManager;
import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

import static io.github.mufasa1976.meetup.springboottest.entities.QPersonalEventEntity.personalEventEntity;

@RequiredArgsConstructor
@Service
@Validated
public class PersonalEventServiceImpl implements PersonalEventService {
  private final PersonalEventRepository repository;
  private final PersonalEventResourceAssembler resourceAssembler;
  private final PagedResourcesAssembler<PersonalEventEntity> pagedResourcesAssembler;
  private final EntityManager entityManager;

  @Override
  public PagedResources<PersonalEvent> findAll(PersonalEventQuery query, Pageable pageable) {
    Page<PersonalEventEntity> page = repository.findAll(
        WhereClauseBuilder.create()
                          .googleLikeIgnoringCase(query.getSearch(), personalEventEntity.header, personalEventEntity.body)
                          .googleLikeIgnoringCase(query.getHeader(), personalEventEntity.header)
                          .googleLikeIgnoringCase(query.getBody(), personalEventEntity.body)
                          .lowerThanOrEqualToStartOfDay(query.getFrom(), personalEventEntity.from, query.getZoneId())
                          .greaterThanOrEqualToEndOfDay(query.getTo(), personalEventEntity.to, query.getZoneId())
                          .buildWhereClause(),
        pageable);
    return pagedResourcesAssembler.toResource(page, resourceAssembler);
  }

  @Override
  public Optional<PersonalEvent> getOne(@NotNull UUID reference) {
    return repository.findOptionalByReference(reference)
                     .map(resourceAssembler::toResource);
  }

  @Override
  @Transactional
  public void create(@NotNull PersonalEvent personalEvent) {
    Optional.of(personalEvent)
            .map(pe -> PersonalEventEntity.builder()
                                          .from(personalEvent.getFrom())
                                          .to(personalEvent.getTo())
                                          .header(personalEvent.getHeader())
                                          .body(personalEvent.getBody())
                                          .build())
            .map(repository::saveAndFlush)
            .ifPresent(entityManager::refresh);
  }

  @Override
  @Transactional
  public void update(@NotNull UUID reference, @NotNull PersonalEvent personalEvent) {
    repository.findOptionalByReference(reference)
              .ifPresentOrElse(
                  update(personalEvent),
                  () -> { throw RowNotFoundException.byReference(PersonalEventEntity.class, reference); });
  }

  private Consumer<PersonalEventEntity> update(PersonalEvent personalEvent) {
    return personalEventEntity -> {
      personalEventEntity.setFrom(personalEvent.getFrom());
      personalEventEntity.setTo(personalEvent.getTo());
      personalEventEntity.setHeader(personalEvent.getHeader());
      personalEventEntity.setBody(personalEvent.getBody());
      repository.saveAndFlush(personalEventEntity);
      entityManager.refresh(personalEventEntity);
    };
  }

  @Override
  @Transactional
  public void delete(@NotNull UUID reference) {
    repository.findOptionalByReference(reference)
              .ifPresentOrElse(
                  repository::delete,
                  () -> { throw RowNotFoundException.byReference(PersonalEventEntity.class, reference); });
  }
}
