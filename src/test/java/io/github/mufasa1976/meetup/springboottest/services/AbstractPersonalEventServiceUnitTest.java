package io.github.mufasa1976.meetup.springboottest.services;

import io.github.mufasa1976.meetup.springboottest.assembler.PersonalEventResourceAssembler;
import io.github.mufasa1976.meetup.springboottest.domains.PersonalEvent;
import io.github.mufasa1976.meetup.springboottest.entities.PersonalEventEntity;
import io.github.mufasa1976.meetup.springboottest.exceptions.RowNotFoundException;
import io.github.mufasa1976.meetup.springboottest.queries.PersonalEventQuery;
import io.github.mufasa1976.meetup.springboottest.repositories.PersonalEventRepository;
import io.github.mufasa1976.meetup.springboottest.representation.rest.PersonalEventRestController;
import io.github.mufasa1976.meetup.springboottest.utils.WhereBuilder;
import org.junit.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

abstract class AbstractPersonalEventServiceUnitTest {
  private static final ZoneId VIENNA = ZoneId.of("Europe/Vienna");

  abstract protected PersonalEventRepository getRepository();

  abstract protected PersonalEventResourceAssembler getResourceAssembler();

  abstract protected PagedResourcesAssembler<PersonalEventEntity> getPagedResourceAssembler();

  abstract protected EntityManager getEntityManager();

  abstract protected PersonalEventService getService();

  @Test
  public void findAll_OK() {
    final UUID uuid1 = UUID.randomUUID();
    final UUID uuid2 = UUID.randomUUID();
    final PageImpl<PersonalEventEntity> page = new PageImpl<>(
        Stream.of(
            PersonalEventEntity.builder()
                               .reference(uuid1)
                               .from(LocalDateTime.of(2019, 1, 19, 23, 48).atZone(VIENNA).toOffsetDateTime())
                               .to(LocalDateTime.of(2019, 1, 19, 23, 49).atZone(VIENNA).toOffsetDateTime())
                               .header("some Header")
                               .body("some Body")
                               .build(),
            PersonalEventEntity.builder()
                               .reference(uuid2)
                               .from(LocalDateTime.of(2019, 1, 19, 23, 50).atZone(VIENNA).toOffsetDateTime())
                               .to(LocalDateTime.of(2019, 1, 19, 23, 51).atZone(VIENNA).toOffsetDateTime())
                               .header("some other Header")
                               .body("some other Body")
                               .build()
        ).collect(Collectors.toList()), Pageable.unpaged(), 2);
    when(getRepository().findAll(eq(WhereBuilder.withoutAnyFilter()), eq(PageRequest.of(0, 20))))
        .thenReturn(page);
    when(getPagedResourceAssembler().toResource(eq(page), eq(getResourceAssembler())))
        .thenReturn(new PagedResources<>(
            page.getContent()
                .stream()
                .map(entity -> PersonalEvent.builder()
                                            .link(linkTo(methodOn(PersonalEventRestController.class).getOne(entity.getReference())).withSelfRel())
                                            .from(entity.getFrom())
                                            .to(entity.getTo())
                                            .header(entity.getHeader())
                                            .body(entity.getBody())
                                            .build())
                .collect(Collectors.toList()),
            new PagedResources.PageMetadata(20, 0, page.getNumberOfElements())));

    assertThat(getService().findAll(PersonalEventQuery.builder().build(), PageRequest.of(0, 20)))
        .extracting(
            PersonalEvent::getFrom,
            PersonalEvent::getTo,
            PersonalEvent::getHeader,
            PersonalEvent::getBody,
            PersonalEvent::getLinks)
        .containsOnly(
            tuple(
                LocalDateTime.of(2019, 1, 19, 23, 48).atZone(VIENNA).toOffsetDateTime(),
                LocalDateTime.of(2019, 1, 19, 23, 49).atZone(VIENNA).toOffsetDateTime(),
                "some Header",
                "some Body",
                Collections.singletonList(linkTo(methodOn(PersonalEventRestController.class).getOne(uuid1)).withSelfRel())),
            tuple(
                LocalDateTime.of(2019, 1, 19, 23, 50).atZone(VIENNA).toOffsetDateTime(),
                LocalDateTime.of(2019, 1, 19, 23, 51).atZone(VIENNA).toOffsetDateTime(),
                "some other Header",
                "some other Body",
                Collections.singletonList(linkTo(methodOn(PersonalEventRestController.class).getOne(uuid2)).withSelfRel()))
        );
  }

  @Test
  @SuppressWarnings("unchecked")
  public void getOne_OK() {
    final UUID uuid = UUID.randomUUID();
    PersonalEventEntity entity = PersonalEventEntity
        .builder()
        .reference(uuid)
        .from(LocalDateTime.of(2019, 1, 20, 0, 0).atZone(VIENNA).toOffsetDateTime())
        .to(LocalDateTime.of(2019, 1, 20, 0, 1).atZone(VIENNA).toOffsetDateTime())
        .header("some Header")
        .body("some Body")
        .build();
    when(getRepository().findOptionalByReference(eq(uuid)))
        .thenReturn(Optional.of(entity));
    when(getResourceAssembler().toResource(eq(entity)))
        .thenReturn(PersonalEvent
            .builder()
            .from(entity.getFrom())
            .to(entity.getTo())
            .header(entity.getHeader())
            .body(entity.getBody())
            .link(linkTo(methodOn(PersonalEventRestController.class).getOne(entity.getReference())).withSelfRel())
            .build());

    assertThat(getService().getOne(uuid))
        .isPresent()
        .get()
        .extracting(
            PersonalEvent::getFrom,
            PersonalEvent::getTo,
            PersonalEvent::getHeader,
            PersonalEvent::getBody,
            PersonalEvent::getLinks)
        .containsOnly(
            entity.getFrom(),
            entity.getTo(),
            entity.getHeader(),
            entity.getBody(),
            Collections.singletonList(linkTo(methodOn(PersonalEventRestController.class).getOne(entity.getReference())).withSelfRel()));
  }

  @Test
  public void getOne_NOK_noDataFound() {
    final UUID uuid = UUID.randomUUID();
    when(getRepository().findOptionalByReference(eq(uuid)))
        .thenReturn(Optional.empty());

    assertThat(getService().getOne(uuid))
        .isNotPresent();
  }

  @Test
  public void create_OK() {
    getService().create(PersonalEvent
        .builder()
        .from(LocalDateTime.of(2019, 1, 20, 0, 17).atZone(VIENNA).toOffsetDateTime())
        .to(LocalDateTime.of(2019, 1, 20, 0, 18).atZone(VIENNA).toOffsetDateTime())
        .header("some Header")
        .body("some Body")
        .build());

    // can't verify the correct saved Entity because the UUID will be generated randomly at runtime
    // so we only can verify, if the save() Method of the Repository has been called only one time
    // with any PersonalEventEntity (hopefully it would be ours)
    verify(getRepository()).saveAndFlush(any(PersonalEventEntity.class));
  }

  @Test
  public void update_OK() {
    final UUID uuid = UUID.randomUUID();
    PersonalEventEntity entity = PersonalEventEntity
        .builder()
        .reference(uuid)
        .from(LocalDateTime.of(2019, 1, 20, 0, 29).atZone(VIENNA).toOffsetDateTime())
        .to(LocalDateTime.of(2019, 1, 20, 0, 30).atZone(VIENNA).toOffsetDateTime())
        .header("some Header")
        .body("some Body")
        .build();
    when(getRepository().findOptionalByReference(eq(uuid)))
        .thenReturn(Optional.of(entity));

    PersonalEvent expected = PersonalEvent
        .builder()
        .from(LocalDateTime.of(2019, 4, 20, 0, 30).atZone(VIENNA).toOffsetDateTime())
        .to(LocalDateTime.of(2019, 4, 20, 0, 40).atZone(VIENNA).toOffsetDateTime())
        .header("some changed Header")
        .body("some changed Body")
        .build();
    getService().update(uuid, expected);

    verify(getRepository()).saveAndFlush(eq(entity));
    verify(getEntityManager()).refresh(entity);
    assertThat(entity)
        .extracting(
            PersonalEventEntity::getReference,
            PersonalEventEntity::getFrom,
            PersonalEventEntity::getTo,
            PersonalEventEntity::getHeader,
            PersonalEventEntity::getBody)
        .contains(
            uuid,
            expected.getFrom(),
            expected.getTo(),
            expected.getHeader(),
            expected.getBody());
  }

  @Test
  public void update_NOK_noDataFound() {
    try {
      getService().update(UUID.randomUUID(), PersonalEvent.builder().build());
      fail("Should throw %s", RowNotFoundException.class);
    } catch (RowNotFoundException ignored) {
    } finally {
      verify(getRepository(), never()).save(any(PersonalEventEntity.class));
    }
  }

  @Test
  public void delete_OK() {
    final UUID uuid = UUID.randomUUID();
    PersonalEventEntity entity = PersonalEventEntity
        .builder()
        .reference(uuid)
        .from(LocalDateTime.of(2019, 1, 20, 0, 29).atZone(VIENNA).toOffsetDateTime())
        .to(LocalDateTime.of(2019, 1, 20, 0, 30).atZone(VIENNA).toOffsetDateTime())
        .header("some Header")
        .body("some Body")
        .build();
    when(getRepository().findOptionalByReference(eq(uuid)))
        .thenReturn(Optional.of(entity));

    getService().delete(uuid);

    verify(getRepository()).delete(entity);
  }

  @Test
  public void delete_NOK_noDataFound() {
    final UUID uuid = UUID.randomUUID();
    when(getRepository().findOptionalByReference(eq(uuid)))
        .thenReturn(Optional.empty());

    try {
      getService().delete(uuid);
      fail("Should throw %s", RowNotFoundException.class);
    } catch (RowNotFoundException ignored) {
    } finally {
      verify(getRepository(), never()).delete(any(PersonalEventEntity.class));
    }
  }
}
