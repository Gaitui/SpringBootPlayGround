package com.play.ground.demo.dto;

import java.time.LocalDateTime;

public record ApiErrorResponse(
    LocalDateTime timestamp,
    int status,
    String code,
    String message,
    String path
) {
}
