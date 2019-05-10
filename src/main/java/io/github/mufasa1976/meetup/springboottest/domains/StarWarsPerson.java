package io.github.mufasa1976.meetup.springboottest.domains;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.github.mufasa1976.meetup.springboottest.converter.GenderDeserializer;
import lombok.*;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StarWarsPerson {
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
