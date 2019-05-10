package io.github.mufasa1976.meetup.springboottest;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class Routes {
  @NoArgsConstructor(access = PRIVATE)
  public static final class Param {
    public static final String
        REFERENCE = "reference",
        ID = "id",
        AUGEND = "augend",
        ADDEND = "addend",
        MINUEND = "minuend",
        SUBTRAHEND = "subtrahend",
        MULTIPLIER = "multiplier",
        MULTIPLICAND = "multiplicand",
        DIVIDEND = "dividend",
        DIVISOR = "divisor";
  }

  public static final String
      API = "/api",
      PERSONAL_EVENTS = API + "/personal-events",
      PERSONAL_EVENT = PERSONAL_EVENTS + "/{" + Param.REFERENCE + "}",
      PERSON = API + "/persons/{" + Param.ID + "}",
      CALCULATOR = API + "/calculator",
      CALCULATOR_ADD = CALCULATOR + "/add/{" + Param.AUGEND + "}/{" + Param.ADDEND + "}",
      CALCULATOR_SUBTRACT = CALCULATOR + "/subtract/{" + Param.MINUEND + "}/{" + Param.SUBTRAHEND + "}",
      CALCULATOR_MULTIPLY = CALCULATOR + "/multiply/{" + Param.MULTIPLIER + "}/{" + Param.MULTIPLICAND + "}",
      CALCULATOR_DIVIDE = CALCULATOR + "/divide/{" + Param.DIVIDEND + "}/{" + Param.DIVISOR + "}";
}
