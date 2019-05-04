package io.github.mufasa1976.meetup.springboottest.representation.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import io.github.mufasa1976.meetup.springboottest.Routes;
import lombok.Builder;
import lombok.Data;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJson;
import org.springframework.test.web.servlet.MockMvc;

import static io.github.mufasa1976.meetup.springboottest.domains.starwars.Person.Gender.MALE;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureJson
abstract class AbstractStarWarsRestControllerIntegrationTest {
  @Autowired
  private MockMvc web;

  @Autowired
  private ObjectMapper objectMapper;

  @Data
  @Builder
  private static class StarWarsPerson {
    private String name;
    @JsonProperty("birth_year")
    private String birthYear;
    private String gender;
    @JsonProperty("eye_color")
    private String eyeColor;
    @JsonProperty("hair_color")
    private String hairColor;
  }

  @Test
  public void getPerson_OK() throws Exception {
    WireMock.stubFor(WireMock.get(WireMock.urlPathEqualTo("/api/people/1/"))
                             .willReturn(WireMock.okJson(objectMapper.writeValueAsString(
                                 StarWarsPerson.builder()
                                               .name("Luke Skywalker")
                                               .gender("male")
                                               .birthYear("19BCC")
                                               .eyeColor("blue")
                                               .hairColor("brown")
                                               .build()))));

    web.perform(get(Routes.PERSON, "1"))
       .andExpect(status().isOk())
       .andExpect(content().contentType(APPLICATION_JSON_UTF8))
       .andExpect(jsonPath("name").value(is("Luke Skywalker")))
       .andExpect(jsonPath("gender").value(is(MALE.name())))
       .andExpect(jsonPath("birth_year").value(is("19BCC")))
       .andExpect(jsonPath("eye_color").value(is("blue")));
  }

  @Test
  public void getPerson_NOK_noDataFound() throws Exception {
    WireMock.stubFor(WireMock.get(WireMock.urlPathEqualTo("/api/people/1/"))
                             .willReturn(WireMock.notFound()));

    web.perform(get(Routes.PERSON, "1"))
       .andExpect(status().isNotFound());
  }
}
