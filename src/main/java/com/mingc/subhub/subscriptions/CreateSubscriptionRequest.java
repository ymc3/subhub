package com.mingc.subhub.subscriptions;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record CreateSubscriptionRequest(
    @Min(value = 1, message = "userId must be positive")
    long userId,

    @NotBlank(message = "plan is required")
    String plan
) {}
