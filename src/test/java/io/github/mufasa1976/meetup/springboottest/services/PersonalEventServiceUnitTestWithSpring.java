package io.github.mufasa1976.meetup.springboottest.services;

import io.github.mufasa1976.meetup.springboottest.assembler.PersonalEventResourceAssembler;
import io.github.mufasa1976.meetup.springboottest.entities.PersonalEventEntity;
import io.github.mufasa1976.meetup.springboottest.repositories.PersonalEventRepository;
import io.github.mufasa1976.meetup.springboottest.services.impl.PersonalEventServiceImpl;
import lombok.Getter;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = PersonalEventServiceImpl.class)
public class PersonalEventServiceUnitTestWithSpring extends AbstractPersonalEventServiceUnitTest {
  @MockBean
  @Getter
  private PersonalEventRepository repository;

  @MockBean
  @Getter
  private PersonalEventResourceAssembler resourceAssembler;

  @MockBean
  @Getter
  private PagedResourcesAssembler<PersonalEventEntity> pagedResourceAssembler;

  @MockBean
  @Getter
  private EntityManager entityManager;

  @Autowired
  @Getter
  private PersonalEventService service;
}
