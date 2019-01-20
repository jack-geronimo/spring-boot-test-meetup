package io.github.mufasa1976.meetup.springboottest.representation.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.mufasa1976.meetup.springboottest.domains.PersonalEvent;
import io.github.mufasa1976.meetup.springboottest.entities.PersonalEventEntity;
import io.github.mufasa1976.meetup.springboottest.repositories.PersonalEventRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static io.github.mufasa1976.meetup.springboottest.Routes.PERSONAL_EVENT;
import static io.github.mufasa1976.meetup.springboottest.Routes.PERSONAL_EVENTS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.hateoas.MediaTypes.HAL_JSON_UTF8;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureDataJpa
@Transactional
@Rollback
abstract class AbstractPersonalEventRestControllerIntegrationTest {
  private static final String SQL_FILE = "classpath:/sql/findAll.sql";
  private static final ZoneId VIENNA = ZoneId.of("Europe/Vienna");

  @Autowired
  private MockMvc web;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private PersonalEventRepository repository;

  @Test
  @Sql(SQL_FILE)
  public void findAll_OK_unfiltered() throws Exception {
    web.perform(get(PERSONAL_EVENTS)
        .with(httpBasic("user", "password")))
       .andExpect(status().isOk())
       .andExpect(content().contentType(HAL_JSON_UTF8))
       .andExpect(jsonPath("_embedded.personalEvents").isArray())
       .andExpect(jsonPath("_embedded.personalEvents.*", hasSize(5)))
       .andExpect(jsonPath("_embedded.personalEvents[0].from").value(is(LocalDateTime.of(2019, 1, 19, 0, 0).atZone(VIENNA).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))))
       .andExpect(jsonPath("_embedded.personalEvents[0].to").value(is(LocalDateTime.of(2019, 1, 19, 23, 59, 59).atZone(VIENNA).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))))
       .andExpect(jsonPath("_embedded.personalEvents[0].header").value(is("some Header")))
       .andExpect(jsonPath("_embedded.personalEvents[0].body").value(is("some Body")))
       .andExpect(jsonPath("_embedded.personalEvents[0]._links.self.href").value(is(createSelfLink(UUID.fromString("00000000-0000-0000-0000-000000000001")))))
       .andExpect(jsonPath("_embedded.personalEvents[1].from").value(is(LocalDateTime.of(2019, 4, 19, 0, 0).atZone(VIENNA).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))))
       .andExpect(jsonPath("_embedded.personalEvents[1].to").value(is(LocalDateTime.of(2019, 4, 19, 23, 59, 59).atZone(VIENNA).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))))
       .andExpect(jsonPath("_embedded.personalEvents[1].header").value(is("some Header")))
       .andExpect(jsonPath("_embedded.personalEvents[1].body").value(is("some Body")))
       .andExpect(jsonPath("_embedded.personalEvents[1]._links.self.href").value(is(createSelfLink(UUID.fromString("00000000-0000-0000-0000-000000000002")))))
       .andExpect(jsonPath("_embedded.personalEvents[2].from").value(is(LocalDateTime.of(2019, 4, 20, 0, 0).atZone(VIENNA).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))))
       .andExpect(jsonPath("_embedded.personalEvents[2].to").value(is(LocalDateTime.of(2019, 4, 21, 23, 59, 59).atZone(VIENNA).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))))
       .andExpect(jsonPath("_embedded.personalEvents[2].header").value(is("xxx")))
       .andExpect(jsonPath("_embedded.personalEvents[2].body").value(is("XXX")))
       .andExpect(jsonPath("_embedded.personalEvents[2]._links.self.href").value(is(createSelfLink(UUID.fromString("00000000-0000-0000-0000-000000000003")))))
       .andExpect(jsonPath("_embedded.personalEvents[3].from").value(is(LocalDateTime.of(2019, 6, 1, 0, 0).atZone(VIENNA).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))))
       .andExpect(jsonPath("_embedded.personalEvents[3].to").value(is(LocalDateTime.of(2019, 6, 5, 23, 59, 59).atZone(VIENNA).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))))
       .andExpect(jsonPath("_embedded.personalEvents[3].header").value(is("yyy")))
       .andExpect(jsonPath("_embedded.personalEvents[3].body").value(is("YYY")))
       .andExpect(jsonPath("_embedded.personalEvents[3]._links.self.href").value(is(createSelfLink(UUID.fromString("00000000-0000-0000-0000-000000000004")))))
       .andExpect(jsonPath("_embedded.personalEvents[4].from").value(is(LocalDateTime.of(2019, 9, 1, 0, 0).atZone(VIENNA).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))))
       .andExpect(jsonPath("_embedded.personalEvents[4].to").value(is(LocalDateTime.of(2019, 9, 30, 23, 59, 59).atZone(VIENNA).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))))
       .andExpect(jsonPath("_embedded.personalEvents[4].header").value(is("zzz")))
       .andExpect(jsonPath("_embedded.personalEvents[4].body").value(is("ZZZ")))
       .andExpect(jsonPath("_embedded.personalEvents[4]._links.self.href").value(is(createSelfLink(UUID.fromString("00000000-0000-0000-0000-000000000005")))))
       .andExpect(jsonPath("page.totalElements").value(is(5)));
  }

  private String createSelfLink(UUID uuid) {
    return String.format("http://localhost%s/%s", PERSONAL_EVENTS, uuid);
  }

  @Test
  @Sql(SQL_FILE)
  public void findAll_OK_filteredBy_header() throws Exception {
    web.perform(get(PERSONAL_EVENTS)
        .param("header", "XXX")
        .with(httpBasic("user", "password")))
       .andExpect(status().isOk())
       .andExpect(content().contentType(HAL_JSON_UTF8))
       .andExpect(jsonPath("_embedded.personalEvents").isArray())
       .andExpect(jsonPath("_embedded.personalEvents.*", hasSize(1)))
       .andExpect(jsonPath("_embedded.personalEvents[0].from").value(is(LocalDateTime.of(2019, 4, 20, 0, 0).atZone(VIENNA).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))))
       .andExpect(jsonPath("_embedded.personalEvents[0].to").value(is(LocalDateTime.of(2019, 4, 21, 23, 59, 59).atZone(VIENNA).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))))
       .andExpect(jsonPath("_embedded.personalEvents[0].header").value(is("xxx")))
       .andExpect(jsonPath("_embedded.personalEvents[0].body").value(is("XXX")))
       .andExpect(jsonPath("_embedded.personalEvents[0]._links.self.href").value(is(createSelfLink(UUID.fromString("00000000-0000-0000-0000-000000000003")))))
       .andExpect(jsonPath("page.totalElements").value(is(1)));
  }

  @Test
  @Sql(SQL_FILE)
  public void findAll_OK_pageSizeIs_2() throws Exception {
    web.perform(get(PERSONAL_EVENTS)
        .param("size", "2")
        .with(httpBasic("user", "password")))
       .andExpect(status().isOk())
       .andExpect(content().contentType(HAL_JSON_UTF8))
       .andExpect(jsonPath("_embedded.personalEvents").isArray())
       .andExpect(jsonPath("_embedded.personalEvents.*", hasSize(2)))
       .andExpect(jsonPath("_embedded.personalEvents[0].from").value(is(LocalDateTime.of(2019, 1, 19, 0, 0).atZone(VIENNA).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))))
       .andExpect(jsonPath("_embedded.personalEvents[0].to").value(is(LocalDateTime.of(2019, 1, 19, 23, 59, 59).atZone(VIENNA).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))))
       .andExpect(jsonPath("_embedded.personalEvents[0].header").value(is("some Header")))
       .andExpect(jsonPath("_embedded.personalEvents[0].body").value(is("some Body")))
       .andExpect(jsonPath("_embedded.personalEvents[0]._links.self.href").value(is(createSelfLink(UUID.fromString("00000000-0000-0000-0000-000000000001")))))
       .andExpect(jsonPath("_embedded.personalEvents[1].from").value(is(LocalDateTime.of(2019, 4, 19, 0, 0).atZone(VIENNA).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))))
       .andExpect(jsonPath("_embedded.personalEvents[1].to").value(is(LocalDateTime.of(2019, 4, 19, 23, 59, 59).atZone(VIENNA).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))))
       .andExpect(jsonPath("_embedded.personalEvents[1].header").value(is("some Header")))
       .andExpect(jsonPath("_embedded.personalEvents[1].body").value(is("some Body")))
       .andExpect(jsonPath("page.size").value(is(2)))
       .andExpect(jsonPath("page.totalElements").value(is(5)));
  }

  @Test
  public void findAll_NOK() throws Exception {
    web.perform(get(PERSONAL_EVENTS)
        .with(httpBasic("user", "password")))
       .andExpect(status().isOk())
       .andExpect(content().contentType(HAL_JSON_UTF8))
       .andExpect(jsonPath("_embedded.personalEvents").doesNotExist())
       .andExpect(jsonPath("page.totalElements").value(is(0)));
  }

  @Test
  @Sql(SQL_FILE)
  public void getOne_OK() throws Exception {
    web.perform(get(PERSONAL_EVENT, "00000000-0000-0000-0000-000000000003")
        .with(httpBasic("user", "password")))
       .andExpect(status().isOk())
       .andExpect(content().contentType(HAL_JSON_UTF8))
       .andExpect(jsonPath("from").value(is(LocalDateTime.of(2019, 4, 20, 0, 0).atZone(VIENNA).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))))
       .andExpect(jsonPath("to").value(is(LocalDateTime.of(2019, 4, 21, 23, 59, 59).atZone(VIENNA).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))))
       .andExpect(jsonPath("header").value(is("xxx")))
       .andExpect(jsonPath("body").value(is("XXX")))
       .andExpect(jsonPath("_links.self.href").value(is(createSelfLink(UUID.fromString("00000000-0000-0000-0000-000000000003")))));
  }

  @Test
  public void getOne_NOK_noDataFound() throws Exception {
    web.perform(get(PERSONAL_EVENT, UUID.randomUUID())
        .with(httpBasic("user", "password")))
       .andExpect(status().isNotFound());
  }

  @Test
  public void create_OK() throws Exception {
    PersonalEvent expected =
        PersonalEvent.builder()
                     .from(LocalDateTime.of(2019, 1, 19, 22, 36).atZone(VIENNA).toOffsetDateTime())
                     .to(LocalDateTime.of(2019, 1, 19, 22, 37).atZone(VIENNA).toOffsetDateTime())
                     .header("some Header")
                     .body("some Body")
                     .build();

    web.perform(post(PERSONAL_EVENTS)
        .contentType(APPLICATION_JSON_UTF8)
        .content(objectMapper.writeValueAsString(expected))
        .with(csrf())
        .with(httpBasic("user", "password")))
       .andExpect(status().isCreated());

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
  public void update_OK() throws Exception {
    final UUID reference = UUID.fromString("00000000-0000-0000-0000-000000000003");
    PersonalEvent expected =
        PersonalEvent.builder()
                     .from(LocalDateTime.of(2019, 1, 19, 22, 36).atZone(VIENNA).toOffsetDateTime())
                     .to(LocalDateTime.of(2019, 1, 19, 22, 37).atZone(VIENNA).toOffsetDateTime())
                     .header("some new Header")
                     .body("some new Body")
                     .build();

    web.perform(put(PERSONAL_EVENT, reference)
        .contentType(APPLICATION_JSON_UTF8)
        .content(objectMapper.writeValueAsString(expected))
        .with(csrf())
        .with(httpBasic("user", "password")))
       .andExpect(status().isNoContent());

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

  @Test
  public void update_NOK_noDataFound() throws Exception {
    web.perform(put(PERSONAL_EVENT, UUID.randomUUID())
        .contentType(APPLICATION_JSON_UTF8)
        .content(objectMapper.writeValueAsString(PersonalEvent
            .builder()
            .from(OffsetDateTime.now())
            .to(OffsetDateTime.now())
            .header("some Header")
            .build()))
        .with(csrf())
        .with(httpBasic("user", "password")))
       .andExpect(status().isNotFound());
  }

  @Test
  @Sql(SQL_FILE)
  public void delete_OK() throws Exception {
    final UUID reference = UUID.fromString("00000000-0000-0000-0000-000000000003");
    web.perform(delete(PERSONAL_EVENT, reference)
        .with(csrf())
        .with(httpBasic("user", "password")))
       .andExpect(status().isNoContent());

    assertThat(repository.findOptionalByReference(reference))
        .isNotPresent();
  }

  @Test
  public void delete_NOK_noDataFound() throws Exception {
    web.perform(delete(PERSONAL_EVENT, UUID.randomUUID())
        .with(csrf())
        .with(httpBasic("user", "password")))
       .andExpect(status().isNotFound());
  }
}
