package io.github.mufasa1976.meetup.springboottest.domains.starwars;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.github.mufasa1976.meetup.springboottest.converter.GenderDeserializer;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import javax.validation.constraints.NotEmpty;

@Value
@Builder
public class Person {
  @RequiredArgsConstructor
  @JsonDeserialize(using = GenderDeserializer.class)
  public enum Gender {
    MALE("male"),
    FEMALE("female"),
    UNKNOWN("n/a");

    @Getter
    private final String remoteContent;
  }

  @NotEmpty
  private String name;

  @JsonProperty("birth_year")
  private String birthYear;

  @JsonProperty("eye_color")
  private String eyeColor;

  private Gender gender;
}
