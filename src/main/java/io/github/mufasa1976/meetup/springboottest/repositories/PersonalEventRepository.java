package io.github.mufasa1976.meetup.springboottest.repositories;

import io.github.mufasa1976.meetup.springboottest.entities.PersonalEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;
import java.util.UUID;

public interface PersonalEventRepository extends JpaRepository<PersonalEventEntity, Integer>, QuerydslPredicateExecutor<PersonalEventEntity> {
  Optional<PersonalEventEntity> findOptionalByReference(UUID reference);
}
