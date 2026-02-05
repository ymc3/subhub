package com.mingc.subhub.subscriptions;

public record Subscription(
    long id,
    long userId,
    String plan,
    SubscriptionStatus status
) {}
