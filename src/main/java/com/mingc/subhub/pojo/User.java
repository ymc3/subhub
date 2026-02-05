package com.mingc.subhub.pojo;

import java.time.Instant;

public record User(
    long id,
    String name,
    String email,
    Instant createdAt,
    Instant updatedAt
) {}
