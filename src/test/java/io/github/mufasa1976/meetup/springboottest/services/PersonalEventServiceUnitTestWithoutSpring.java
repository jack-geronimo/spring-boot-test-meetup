package io.github.mufasa1976.meetup.springboottest.services;

import io.github.mufasa1976.meetup.springboottest.assembler.PersonalEventResourceAssembler;
import io.github.mufasa1976.meetup.springboottest.entities.PersonalEventEntity;
import io.github.mufasa1976.meetup.springboottest.repositories.PersonalEventRepository;
import lombok.Getter;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.web.PagedResourcesAssembler;

import javax.persistence.EntityManager;

@RunWith(MockitoJUnitRunner.class)
public class PersonalEventServiceUnitTestWithoutSpring extends AbstractPersonalEventServiceUnitTest {
  @Mock
  @Getter
  private PersonalEventRepository repository;

  @Mock
  @Getter
  private PersonalEventResourceAssembler resourceAssembler;

  @Mock
  @Getter
  private PagedResourcesAssembler<PersonalEventEntity> pagedResourceAssembler;

  @Mock
  @Getter
  private EntityManager entityManager;

  @Getter
  private PersonalEventService service;

  @Before
  public void setUp() {
    service = new PersonalEventServiceImpl(repository, resourceAssembler, pagedResourceAssembler, entityManager);
  }
}
