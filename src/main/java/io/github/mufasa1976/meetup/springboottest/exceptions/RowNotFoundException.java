package io.github.mufasa1976.meetup.springboottest.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@ResponseStatus(NOT_FOUND)
public class RowNotFoundException extends RuntimeException {
  public static RowNotFoundException byReference(Class<?> entityClass, UUID reference) {
    return new RowNotFoundException(String.format("No Entity of Type %s is referenced by %s", entityClass.getSimpleName(), reference));
  }

  private RowNotFoundException(String message) {
    super(message);
  }
}
