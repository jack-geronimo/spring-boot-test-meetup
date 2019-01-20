package io.github.mufasa1976.meetup.springboottest.queries;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.ZoneId;

@Value
@AllArgsConstructor(onConstructor = @__(@JsonCreator))
@Builder
public class PersonalEventQuery {
  private String search;
  private ZoneId zoneId;
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate from;
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate to;
  private String header;
  private String body;
}
