package io.github.mufasa1976.meetup.springboottest.representation.ws;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ServerList;
import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;
import io.github.mufasa1976.meetup.springboottest.config.FeignConfiguration;
import io.github.mufasa1976.meetup.springboottest.config.SecurityConfiguration;
import io.github.mufasa1976.meetup.springboottest.config.WebServiceConfiguration;
import io.github.mufasa1976.meetup.springboottest.services.StarWarsService;
import io.github.mufasa1976.meetup.springboottest.services.StarWarsServiceImpl;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.io.IOUtils;
import org.apache.cxf.spring.boot.autoconfigure.CxfAutoConfiguration;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJson;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cloud.contract.wiremock.WireMockSpring;
import org.springframework.cloud.netflix.ribbon.RibbonAutoConfiguration;
import org.springframework.cloud.netflix.ribbon.StaticServerList;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.cloud.openfeign.ribbon.FeignRibbonClientAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.springframework.http.MediaType.TEXT_XML;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureJson
@WithMockUser
public class StarWarsEndpointIntegrationTest {
  @ClassRule
  public static WireMockRule WIREMOCK = new WireMockRule(WireMockSpring.options().dynamicHttpsPort());

  @TestConfiguration
  @Import({FeignConfiguration.class, SecurityConfiguration.class, WebServiceConfiguration.class})
  @ImportAutoConfiguration({FeignAutoConfiguration.class, RibbonAutoConfiguration.class, FeignRibbonClientAutoConfiguration.class, CxfAutoConfiguration.class})
  @ComponentScan(basePackageClasses = StarWarsService.class, useDefaultFilters = false, includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = StarWarsServiceImpl.class))
  @ComponentScan(basePackageClasses = StarWarsEndpoint.class, useDefaultFilters = false, includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = StarWarsEndpointImpl.class))
  public static class Configuration {
    @Bean
    public ServerList<Server> ribbonServerList() {
      return new StaticServerList<>(new Server("localhost", WIREMOCK.port()));
    }
  }

  @Data
  @Builder
  private static class StarWarsTestPerson {
    private String name;
    @JsonProperty("birth_year")
    private String birthYear;
    private String gender;
    @JsonProperty("eye_color")
    private String eyeColor;
    @JsonProperty("hair_color")
    private String hairColor;
  }

  @Autowired
  private WebApplicationContext webApplicationContext;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private ResourceLoader resourceLoader;

  private MockMvc web;

  @Before
  public void setUp() {
    web = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                         .dispatchOptions(true)
                         .alwaysDo(MockMvcResultHandlers.print())
                         .build();
  }

  @Test
  public void test_OK() throws Exception {
    WireMock.stubFor(WireMock.get(WireMock.urlPathEqualTo("/api/people/1/"))
                             .willReturn(WireMock.okJson(objectMapper.writeValueAsString(
                                 StarWarsTestPerson.builder()
                                                   .name("Luke Skywalker")
                                                   .gender("male")
                                                   .birthYear("19BCC")
                                                   .eyeColor("blue")
                                                   .hairColor("brown")
                                                   .build()))));

    web.perform(post("/api/ws/StarWarsService")
        .contentType(TEXT_XML)
        .characterEncoding("utf-8")
        .content(getTemplate("getPerson").execute("1")))
       .andExpect(status().isOk())
       .andExpect(xpath("/*[local-name() = 'getPersonResponse']/return/birthYear").string("19BCC"));
  }

  private Template getTemplate(String templateName) throws IOException {
    Resource resource = resourceLoader.getResource(String.format("classpath:/ws/%s.mustache", templateName));
    try (InputStream inputStream = resource.getInputStream()) {
      return Mustache.compiler().compile(IOUtils.toString(inputStream, StandardCharsets.UTF_8));
    }
  }
}
