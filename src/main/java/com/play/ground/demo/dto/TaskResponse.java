package com.play.ground.demo.dto;

public record TaskResponse(
    Long id,
    String title,
    String description,
    boolean completed
) {
}
