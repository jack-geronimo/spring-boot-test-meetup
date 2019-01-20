package io.github.mufasa1976.meetup.springboottest.representation.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.mufasa1976.meetup.springboottest.domains.PersonalEvent;
import io.github.mufasa1976.meetup.springboottest.entities.PersonalEventEntity;
import io.github.mufasa1976.meetup.springboottest.exceptions.RowNotFoundException;
import io.github.mufasa1976.meetup.springboottest.queries.PersonalEventQuery;
import io.github.mufasa1976.meetup.springboottest.services.PersonalEventService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.PagedResources;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.github.mufasa1976.meetup.springboottest.Routes.PERSONAL_EVENT;
import static io.github.mufasa1976.meetup.springboottest.Routes.PERSONAL_EVENTS;
import static io.github.mufasa1976.meetup.springboottest.Routes.Param.REFERENCE;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.hateoas.MediaTypes.HAL_JSON_UTF8;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(PersonalEventRestController.class)
public class PersonalEventRestControllerUnitTest {
  private static final ZoneId VIENNA = ZoneId.of("Europe/Vienna");

  @MockBean
  private PersonalEventService service;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private MockMvc web;

  @Test
  public void findAll_OK_filteredBy_search() throws Exception {
    final UUID reference1 = UUID.randomUUID();
    final UUID reference2 = UUID.randomUUID();
    List<PersonalEvent> personalEvents = Stream.of(
        PersonalEvent.builder()
                     .from(LocalDateTime.of(2019, 1, 19, 23, 48).atZone(VIENNA).toOffsetDateTime())
                     .to(LocalDateTime.of(2019, 1, 19, 23, 49).atZone(VIENNA).toOffsetDateTime())
                     .header("some Header")
                     .body("some Body")
                     .link(linkTo(methodOn(PersonalEventRestController.class).getOne(reference1)).withSelfRel())
                     .build(),
        PersonalEvent.builder()
                     .from(LocalDateTime.of(2019, 1, 19, 23, 50).atZone(VIENNA).toOffsetDateTime())
                     .to(LocalDateTime.of(2019, 1, 19, 23, 51).atZone(VIENNA).toOffsetDateTime())
                     .header("some other Header")
                     .body("some other Body")
                     .link(linkTo(methodOn(PersonalEventRestController.class).getOne(reference2)).withSelfRel())
                     .build()
    ).collect(Collectors.toList());
    when(service.findAll(
        eq(PersonalEventQuery
            .builder()
            .search("some")
            .build()),
        eq(PageRequest.of(0, 20))))
        .thenReturn(new PagedResources<>(personalEvents, new PagedResources.PageMetadata(20, 0, personalEvents.size())));

    web.perform(get(PERSONAL_EVENTS)
        .param("search", "some")
        .with(httpBasic("user", "password")))
       .andExpect(status().isOk())
       .andExpect(content().contentType(HAL_JSON_UTF8))
       .andExpect(jsonPath("_embedded.personalEvents").isArray())
       .andExpect(jsonPath("_embedded.personalEvents.*", hasSize(2)))
       .andExpect(jsonPath("_embedded.personalEvents[0].from").value(is(personalEvents.get(0).getFrom().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))))
       .andExpect(jsonPath("_embedded.personalEvents[0].to").value(is(personalEvents.get(0).getTo().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))))
       .andExpect(jsonPath("_embedded.personalEvents[0].header").value(is(personalEvents.get(0).getHeader())))
       .andExpect(jsonPath("_embedded.personalEvents[0].body").value(is(personalEvents.get(0).getBody())))
       .andExpect(jsonPath("_embedded.personalEvents[0]._links.self.href").value(is(createSelfLink(reference1))))
       .andExpect(jsonPath("_embedded.personalEvents[1].from").value(is(personalEvents.get(1).getFrom().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))))
       .andExpect(jsonPath("_embedded.personalEvents[1].to").value(is(personalEvents.get(1).getTo().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))))
       .andExpect(jsonPath("_embedded.personalEvents[1].header").value(is(personalEvents.get(1).getHeader())))
       .andExpect(jsonPath("_embedded.personalEvents[1].body").value(is(personalEvents.get(1).getBody())))
       .andExpect(jsonPath("_embedded.personalEvents[1]._links.self.href").value(is(createSelfLink(reference2))))
       .andExpect(jsonPath("page.number").value(is(0)))
       .andExpect(jsonPath("page.size").value(is(20)))
       .andExpect(jsonPath("page.totalElements").value(is(personalEvents.size())));
  }

  private String createSelfLink(UUID reference) {
    return PERSONAL_EVENT.replace("{" + REFERENCE + "}", reference.toString());
  }

  @Test
  public void findAll_NOK() throws Exception {
    when(service.findAll(eq(PersonalEventQuery.builder().build()), eq(PageRequest.of(0, 20))))
        .thenReturn(new PagedResources<>(Collections.emptyList(), new PagedResources.PageMetadata(20, 0, 0)));

    web.perform(get(PERSONAL_EVENTS)
        .with(httpBasic("user", "password")))
       .andExpect(status().isOk())
       .andExpect(content().contentType(HAL_JSON_UTF8))
       .andExpect(jsonPath("_embedded.personalEvents").doesNotExist())
       .andExpect(jsonPath("page.number").value(is(0)))
       .andExpect(jsonPath("page.size").value(is(20)))
       .andExpect(jsonPath("page.totalElements").value(is(0)));
  }

  @Test
  public void getOne_OK() throws Exception {
    final UUID reference = UUID.randomUUID();
    PersonalEvent expected = PersonalEvent
        .builder()
        .from(LocalDateTime.of(2019, 1, 19, 23, 48).atZone(VIENNA).toOffsetDateTime())
        .to(LocalDateTime.of(2019, 1, 19, 23, 49).atZone(VIENNA).toOffsetDateTime())
        .header("some Header")
        .body("some Body")
        .link(linkTo(methodOn(PersonalEventRestController.class).getOne(reference)).withSelfRel())
        .build();
    when(service.getOne(reference)).thenReturn(Optional.of(expected));

    web.perform(get(PERSONAL_EVENT, reference)
        .with(httpBasic("user", "password")))
       .andExpect(status().isOk())
       .andExpect(content().contentType(HAL_JSON_UTF8))
       .andExpect(jsonPath("from").value(is(expected.getFrom().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))))
       .andExpect(jsonPath("to").value(is(expected.getTo().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))))
       .andExpect(jsonPath("header").value(is(expected.getHeader())))
       .andExpect(jsonPath("body").value(is(expected.getBody())))
       .andExpect(jsonPath("_links.self.href").value(is(createSelfLink(reference))));
  }

  @Test
  public void getOne_NOK_noDataFound() throws Exception {
    web.perform(get(PERSONAL_EVENT, UUID.randomUUID())
        .with(httpBasic("user", "password")))
       .andExpect(status().isNotFound());
  }

  @Test
  public void create_OK() throws Exception {
    // Spring MVC uses UTC-zoned OffsetDateTime. So we have to convert to UTC just before so that the expected Object is really the expected (because Mockito doesn't verify
    // OffsetDateTime the correct Way).
    PersonalEvent expected =
        PersonalEvent.builder()
                     .from(LocalDateTime.of(2019, 1, 19, 22, 36).atZone(VIENNA).toOffsetDateTime().withOffsetSameInstant(ZoneOffset.UTC))
                     .to(LocalDateTime.of(2019, 1, 19, 22, 37).atZone(VIENNA).toOffsetDateTime().withOffsetSameInstant(ZoneOffset.UTC))
                     .header("some Header")
                     .body("some Body")
                     .build();
    web.perform(post(PERSONAL_EVENTS)
        .contentType(APPLICATION_JSON_UTF8)
        .content(objectMapper.writeValueAsString(expected))
        .with(csrf())
        .with(httpBasic("user", "password")))
       .andExpect(status().isCreated());

    verify(service).create(expected);
  }

  @Test
  public void update_OK() throws Exception {
    final UUID reference = UUID.randomUUID();

    // Spring MVC uses UTC-zoned OffsetDateTime. So we have to convert to UTC just before so that the expected Object is really the expected (because Mockito doesn't verify
    // OffsetDateTime the correct Way).
    PersonalEvent expected = PersonalEvent
        .builder()
        .from(OffsetDateTime.now().withOffsetSameInstant(ZoneOffset.UTC))
        .to(OffsetDateTime.now().withOffsetSameInstant(ZoneOffset.UTC))
        .header("some Header")
        .build();
    doNothing().when(service).update(reference, expected);

    web.perform(put(PERSONAL_EVENT, reference)
        .contentType(APPLICATION_JSON_UTF8)
        .content(objectMapper.writeValueAsString(expected))
        .with(csrf())
        .with(httpBasic("user", "password")))
       .andExpect(status().isNoContent());
  }

  @Test
  public void update_NOK_noDataFound() throws Exception {
    final UUID reference = UUID.randomUUID();

    // Spring MVC uses UTC-zoned OffsetDateTime. So we have to convert to UTC just before so that the expected Object is really the expected (because Mockito doesn't verify
    // OffsetDateTime the correct Way).
    PersonalEvent expected = PersonalEvent
        .builder()
        .from(OffsetDateTime.now().withOffsetSameInstant(ZoneOffset.UTC))
        .to(OffsetDateTime.now().withOffsetSameInstant(ZoneOffset.UTC))
        .header("some Header")
        .build();
    doThrow(RowNotFoundException.byReference(PersonalEventEntity.class, reference)).when(service).update(reference, expected);

    web.perform(put(PERSONAL_EVENT, reference)
        .contentType(APPLICATION_JSON_UTF8)
        .content(objectMapper.writeValueAsString(expected))
        .with(csrf())
        .with(httpBasic("user", "password")))
       .andExpect(status().isNotFound());
  }

  @Test
  public void delete_OK() throws Exception {
    final UUID reference = UUID.randomUUID();
    doNothing().when(service).delete(reference);

    web.perform(delete(PERSONAL_EVENT, reference)
        .with(csrf())
        .with(httpBasic("user", "password")))
       .andExpect(status().isNoContent());
  }

  @Test
  public void delete_NOK_noDataFound() throws Exception {
    final UUID reference = UUID.randomUUID();
    doThrow(RowNotFoundException.byReference(PersonalEventEntity.class, reference)).when(service).delete(reference);

    web.perform(delete(PERSONAL_EVENT, reference)
        .with(csrf())
        .with(httpBasic("user", "password")))
       .andExpect(status().isNotFound());
  }
}
