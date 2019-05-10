package io.github.mufasa1976.meetup.springboottest.converter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import io.github.mufasa1976.meetup.springboottest.domains.StarWarsPerson;

import java.io.IOException;

public class GenderDeserializer extends JsonDeserializer<StarWarsPerson.Gender> {
  @Override
  public StarWarsPerson.Gender deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
    final String encodedGender = p.getValueAsString();
    for (StarWarsPerson.Gender gender : StarWarsPerson.Gender.values()) {
      if (gender.getRemoteContent().equalsIgnoreCase(encodedGender)) {
        return gender;
      }
    }
    return null;
  }
}
