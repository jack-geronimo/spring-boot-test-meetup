package io.github.mufasa1976.meetup.springboottest.assembler;

import io.github.mufasa1976.meetup.springboottest.domains.PersonalEvent;
import io.github.mufasa1976.meetup.springboottest.entities.PersonalEventEntity;
import io.github.mufasa1976.meetup.springboottest.representation.rest.PersonalEventRestController;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class PersonalEventResourceAssembler implements ResourceAssembler<PersonalEventEntity, PersonalEvent> {
  @Override
  public PersonalEvent toResource(PersonalEventEntity personalEventEntity) {
    return PersonalEvent.builder()
                        .from(personalEventEntity.getFrom())
                        .to(personalEventEntity.getTo())
                        .header(personalEventEntity.getHeader())
                        .body(personalEventEntity.getBody())
                        .link(linkTo(methodOn(PersonalEventRestController.class).getOne(personalEventEntity.getReference())).withSelfRel())
                        .build();
  }
}
