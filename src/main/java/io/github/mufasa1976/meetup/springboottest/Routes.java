package io.github.mufasa1976.meetup.springboottest;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class Routes {
  @NoArgsConstructor(access = PRIVATE)
  public static final class Param {
    public static final String
        REFERENCE = "reference",
        ID = "id";
  }

  public static final String
      API = "/api",
      PERSONAL_EVENTS = API + "/personal-events",
      PERSONAL_EVENT = PERSONAL_EVENTS + "/{" + Param.REFERENCE + "}",
      PERSON = API + "/persons/{" + Param.ID + "}";
}
