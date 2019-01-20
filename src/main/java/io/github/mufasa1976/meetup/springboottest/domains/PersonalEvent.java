package io.github.mufasa1976.meetup.springboottest.domains;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Singular;
import lombok.Value;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Value
@EqualsAndHashCode(callSuper = false)
@Relation(value = "personalEvent", collectionRelation = "personalEvents")
public class PersonalEvent extends ResourceSupport {
  @NotNull
  private OffsetDateTime from;
  private OffsetDateTime to;
  @NotNull
  @Size(max = 255)
  private String header;
  private String body;

  @Builder
  @JsonCreator
  public PersonalEvent(
      @JsonProperty("from") OffsetDateTime from,
      @JsonProperty("to") OffsetDateTime to,
      @JsonProperty("header") String header,
      @JsonProperty("body") String body,
      @Singular @JsonProperty("links") List<Link> links) {
    this.from = from;
    this.to = to;
    this.header = header;
    this.body = body;
    Optional.ofNullable(links)
            .stream()
            .flatMap(Collection::stream)
            .forEach(super::add);
  }
}
