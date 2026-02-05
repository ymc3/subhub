package com.mingc.subhub.users;

import java.time.Instant;

public record User(
    long id,
    String name,
    String email,
    Instant createdAt,
    Instant updatedAt
) {}
