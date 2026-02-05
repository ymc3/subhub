package com.mingc.subhub.pojo;

import jakarta.validation.constraints.NotNull;

public record UpdateSubscriptionStatusRequest(
    @NotNull(message = "status is required")
    SubscriptionStatus status
) {}
