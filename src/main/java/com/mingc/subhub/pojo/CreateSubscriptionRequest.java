package com.mingc.subhub.pojo;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CreateSubscriptionRequest(
    @Min(value = 1, message = "userId must be positive")
    long userId,

    @NotNull(message = "plan is required")
    SubscriptionPlan plan
) {}
