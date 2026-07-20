package com.play.ground.demo.dto;

import java.time.LocalDateTime;
import java.util.Map;

public record ValidationErrorResponse(
    LocalDateTime timestamp,
    int status,
    String code,
    String message,
    Map<String, String> errors
) {
}
