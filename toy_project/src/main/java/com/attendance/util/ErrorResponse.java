package com.attendance.util;

import org.springframework.http.HttpStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ErrorResponse {

  private final HttpStatus status;
  private final String message;

}
