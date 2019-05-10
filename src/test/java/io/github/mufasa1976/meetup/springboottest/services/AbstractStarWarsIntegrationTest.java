package io.github.mufasa1976.meetup.springboottest.services;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import lombok.Builder;
import lombok.Data;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJson;

import static io.github.mufasa1976.meetup.springboottest.domains.StarWarsPerson.Gender.MALE;
import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureJson
abstract class AbstractStarWarsIntegrationTest {
  @Autowired
  private StarWarsService starWarsService;

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
  public void test_OK() throws Exception {
    WireMock.stubFor(WireMock.get(WireMock.urlPathEqualTo("/api/people/1/"))
                             .willReturn(WireMock.okJson(objectMapper.writeValueAsString(
                                 AbstractStarWarsIntegrationTest.StarWarsPerson.builder()
                                                                               .name("Luke Skywalker")
                                                                               .gender("male")
                                                                               .birthYear("19BCC")
                                                                               .eyeColor("blue")
                                                                               .hairColor("brown")
                                                                               .build()))));

    assertThat(starWarsService.getPerson("1"))
        .isPresent()
        .contains(io.github.mufasa1976.meetup.springboottest.domains.StarWarsPerson.builder()
                                                                                   .name("Luke Skywalker")
                                                                                   .gender(MALE)
                                                                                   .birthYear("19BCC")
                                                                                   .eyeColor("blue")
                                                                                   .build());

    WireMock.verify(WireMock.getRequestedFor(WireMock.urlPathEqualTo("/api/people/1/")));
  }

  @Test
  public void test_NOK_noDataFound() throws Exception {
    WireMock.stubFor(WireMock.get(WireMock.urlPathEqualTo("/api/people/1/"))
                             .willReturn(WireMock.notFound()));

    assertThat(starWarsService.getPerson("1")).isNotPresent();
  }
}
