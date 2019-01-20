package io.github.mufasa1976.meetup.springboottest.services;

import io.github.mufasa1976.meetup.springboottest.domains.PersonalEvent;
import io.github.mufasa1976.meetup.springboottest.entities.PersonalEventEntity;
import io.github.mufasa1976.meetup.springboottest.exceptions.RowNotFoundException;
import io.github.mufasa1976.meetup.springboottest.queries.PersonalEventQuery;
import io.github.mufasa1976.meetup.springboottest.repositories.PersonalEventRepository;
import io.github.mufasa1976.meetup.springboottest.representation.rest.PersonalEventRestController;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.UUID;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@WithMockUser
abstract class AbstractPersonalEventServiceIntegrationTest {
  private static final String SQL_FILE = "classpath:/sql/findAll.sql";
  private static final ZoneId VIENNA = ZoneId.of("Europe/Vienna");

  @Autowired
  private PersonalEventService service;

  @Autowired
  private PersonalEventRepository repository;

  @Test
  @Sql(SQL_FILE)
  public void findAll_OK_unfiltered() {
    assertThat(service.findAll(PersonalEventQuery.builder().build(), Pageable.unpaged()))
        .hasSize(5)
        .extracting(
            PersonalEvent::getFrom,
            PersonalEvent::getTo,
            PersonalEvent::getHeader,
            PersonalEvent::getBody,
            PersonalEvent::getLinks)
        .containsOnly(
            tuple(
                LocalDateTime.of(2019, 1, 19, 0, 0, 0).atOffset(ZoneOffset.of("+01:00")),
                LocalDateTime.of(2019, 1, 19, 23, 59, 59).atOffset(ZoneOffset.of("+01:00")),
                "some Header",
                "some Body",
                singletonList(linkTo(methodOn(PersonalEventRestController.class).getOne(UUID.fromString("00000000-0000-0000-0000-000000000001"))).withSelfRel())),
            tuple(
                LocalDateTime.of(2019, 4, 19, 0, 0, 0).atOffset(ZoneOffset.of("+02:00")),
                LocalDateTime.of(2019, 4, 19, 23, 59, 59).atOffset(ZoneOffset.of("+02:00")),
                "some Header",
                "some Body",
                singletonList(linkTo(methodOn(PersonalEventRestController.class).getOne(UUID.fromString("00000000-0000-0000-0000-000000000002"))).withSelfRel())),
            tuple(
                LocalDateTime.of(2019, 4, 20, 0, 0, 0).atOffset(ZoneOffset.of("+02:00")),
                LocalDateTime.of(2019, 4, 21, 23, 59, 59).atOffset(ZoneOffset.of("+02:00")),
                "xxx",
                "XXX",
                singletonList(linkTo(methodOn(PersonalEventRestController.class).getOne(UUID.fromString("00000000-0000-0000-0000-000000000003"))).withSelfRel())),
            tuple(
                LocalDateTime.of(2019, 6, 1, 0, 0, 0).atOffset(ZoneOffset.of("+02:00")),
                LocalDateTime.of(2019, 6, 5, 23, 59, 59).atOffset(ZoneOffset.of("+02:00")),
                "yyy",
                "YYY",
                singletonList(linkTo(methodOn(PersonalEventRestController.class).getOne(UUID.fromString("00000000-0000-0000-0000-000000000004"))).withSelfRel())),
            tuple(
                LocalDateTime.of(2019, 9, 1, 0, 0, 0).atOffset(ZoneOffset.of("+02:00")),
                LocalDateTime.of(2019, 9, 30, 23, 59, 59).atOffset(ZoneOffset.of("+02:00")),
                "zzz",
                "ZZZ",
                singletonList(linkTo(methodOn(PersonalEventRestController.class).getOne(UUID.fromString("00000000-0000-0000-0000-000000000005"))).withSelfRel())));
  }

  @Test
  @Sql(SQL_FILE)
  public void findAll_OK_filterBy_header() {
    assertThat(service.findAll(PersonalEventQuery
        .builder()
        .header("XXX")
        .build(), Pageable.unpaged()))
        .hasSize(1)
        .extracting(
            PersonalEvent::getFrom,
            PersonalEvent::getTo,
            PersonalEvent::getHeader,
            PersonalEvent::getBody,
            PersonalEvent::getLinks)
        .containsOnly(
            tuple(
                LocalDateTime.of(2019, 4, 20, 0, 0, 0).atOffset(ZoneOffset.of("+02:00")),
                LocalDateTime.of(2019, 4, 21, 23, 59, 59).atOffset(ZoneOffset.of("+02:00")),
                "xxx",
                "XXX",
                singletonList(linkTo(methodOn(PersonalEventRestController.class).getOne(UUID.fromString("00000000-0000-0000-0000-000000000003"))).withSelfRel())));
  }

  @Test
  @Sql(SQL_FILE)
  public void findAll_OK_pagedSizeIs_2() {
    assertThat(service.findAll(PersonalEventQuery.builder().build(), PageRequest.of(0, 2)))
        .hasSize(2)
        .extracting(
            PersonalEvent::getFrom,
            PersonalEvent::getTo,
            PersonalEvent::getHeader,
            PersonalEvent::getBody,
            PersonalEvent::getLinks)
        .containsOnly(
            tuple(
                LocalDateTime.of(2019, 1, 19, 0, 0, 0).atOffset(ZoneOffset.of("+01:00")),
                LocalDateTime.of(2019, 1, 19, 23, 59, 59).atOffset(ZoneOffset.of("+01:00")),
                "some Header",
                "some Body",
                singletonList(linkTo(methodOn(PersonalEventRestController.class).getOne(UUID.fromString("00000000-0000-0000-0000-000000000001"))).withSelfRel())),
            tuple(
                LocalDateTime.of(2019, 4, 19, 0, 0, 0).atOffset(ZoneOffset.of("+02:00")),
                LocalDateTime.of(2019, 4, 19, 23, 59, 59).atOffset(ZoneOffset.of("+02:00")),
                "some Header",
                "some Body",
                singletonList(linkTo(methodOn(PersonalEventRestController.class).getOne(UUID.fromString("00000000-0000-0000-0000-000000000002"))).withSelfRel())));
  }

  @Test
  public void findAll_NOK() {
    assertThat(service.findAll(PersonalEventQuery.builder().build(), Pageable.unpaged()))
        .isEmpty();
  }

  @Test
  @Sql(SQL_FILE)
  public void getOne_OK() {
    assertThat(service.getOne(UUID.fromString("00000000-0000-0000-0000-000000000001")))
        .isNotEmpty()
        .contains(PersonalEvent
            .builder()
            .from(LocalDateTime.of(2019, 1, 19, 0, 0, 0).atOffset(ZoneOffset.of("+01:00")))
            .to(LocalDateTime.of(2019, 1, 19, 23, 59, 59).atOffset(ZoneOffset.of("+01:00")))
            .header("some Header")
            .body("some Body")
            .build());
  }

  @Test
  public void getOne_NOK_noDataFound() {
    assertThat(service.getOne(UUID.randomUUID()))
        .isEmpty();
  }

  @Test
  public void create_OK() {
    PersonalEvent expected =
        PersonalEvent.builder()
                     .from(LocalDateTime.of(2019, 1, 19, 22, 36).atZone(VIENNA).toOffsetDateTime())
                     .to(LocalDateTime.of(2019, 1, 19, 22, 37).atZone(VIENNA).toOffsetDateTime())
                     .header("some Header")
                     .body("some Body")
                     .build();
    service.create(expected);

    assertThat(repository.findAll())
        .hasSize(1)
        .extracting(
            PersonalEventEntity::getFrom,
            PersonalEventEntity::getTo,
            PersonalEventEntity::getHeader,
            PersonalEventEntity::getBody,
            PersonalEventEntity::getLastModifiedBy)
        .containsOnly(tuple(
            expected.getFrom(),
            expected.getTo(),
            expected.getHeader(),
            expected.getBody(),
            "user"));
  }

  @Test
  @Sql(SQL_FILE)
  @SuppressWarnings("unchecked")
  public void update_OK() {
    final UUID reference = UUID.fromString("00000000-0000-0000-0000-000000000003");
    PersonalEvent expected =
        PersonalEvent.builder()
                     .from(LocalDateTime.of(2019, 1, 19, 22, 36).atZone(VIENNA).toOffsetDateTime())
                     .to(LocalDateTime.of(2019, 1, 19, 22, 37).atZone(VIENNA).toOffsetDateTime())
                     .header("some new Header")
                     .body("some new Body")
                     .build();
    service.update(reference, expected);

    assertThat(repository.findOptionalByReference(reference))
        .isPresent()
        .get()
        .extracting(
            PersonalEventEntity::getFrom,
            PersonalEventEntity::getTo,
            PersonalEventEntity::getHeader,
            PersonalEventEntity::getBody,
            PersonalEventEntity::getLastModifiedBy)
        .containsOnly(
            expected.getFrom(),
            expected.getTo(),
            expected.getHeader(),
            expected.getBody(),
            "user");
  }

  @Test(expected = RowNotFoundException.class)
  public void update_NOK_notFound() {
    service.update(UUID.randomUUID(), PersonalEvent.builder().build());
  }

  @Test
  @Sql(SQL_FILE)
  public void delete_OK() {
    final UUID reference = UUID.fromString("00000000-0000-0000-0000-000000000003");
    service.delete(reference);

    assertThat(repository.findOptionalByReference(reference))
        .isNotPresent();
  }

  @Test(expected = RowNotFoundException.class)
  public void delete_NOK_notFound() {
    service.delete(UUID.randomUUID());
  }
}
