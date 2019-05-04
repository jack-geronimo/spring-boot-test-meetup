package io.github.mufasa1976.meetup.springboottest.services;

import io.github.mufasa1976.meetup.springboottest.cients.StarWarsClient;
import io.github.mufasa1976.meetup.springboottest.domains.starwars.Person;
import org.junit.Test;

import java.util.Optional;

import static io.github.mufasa1976.meetup.springboottest.domains.starwars.Person.Gender.MALE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

abstract class AbstractStarWarsServiceUnitTest {
  protected abstract StarWarsClient getClient();

  protected abstract StarWarsService getService();

  @Test
  @SuppressWarnings("unchecked")
  public void getPerson_OK() throws Exception {
    when(getClient().getPerson("1"))
        .thenReturn(Optional.of(Person.builder()
                                      .name("Luke Skywalker")
                                      .gender(MALE)
                                      .birthYear("19BBY")
                                      .eyeColor("blue")
                                      .build()));

    assertThat(getService().getPerson("1")).isPresent()
                                           .get()
                                           .extracting(Person::getName, Person::getGender, Person::getBirthYear, Person::getEyeColor)
                                           .containsOnly("Luke Skywalker", MALE, "19BBY", "blue");
  }

  @Test
  public void getPerson_NOK_noDataFound() throws Exception {
    when(getClient().getPerson(anyString())).thenReturn(Optional.empty());

    assertThat(getService().getPerson("1")).isNotPresent();
  }
}
