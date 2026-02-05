package com.mingc.subhub.pojo;

import java.time.Instant;

public record Subscription(
    long id,
    long userId,
    SubscriptionPlan plan,
    SubscriptionStatus status,
    Instant createdAt,
    Instant updatedAt,
    Instant expiresAt
) {}
