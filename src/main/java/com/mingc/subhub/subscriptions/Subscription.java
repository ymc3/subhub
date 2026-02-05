package com.mingc.subhub.subscriptions;

import java.time.Instant;

public record Subscription(
    long id,
    long userId,
    String plan,
    SubscriptionStatus status,
    Instant createdAt,
    Instant updatedAt
) {}
