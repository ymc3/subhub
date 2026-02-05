package com.mingc.subhub.subscriptions;

import jakarta.validation.constraints.NotNull;

public record UpdateSubscriptionStatusRequest(
    @NotNull(message = "status is required")
    SubscriptionStatus status
) {}
