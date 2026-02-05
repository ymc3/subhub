package com.mingc.subhub.pojo;

import jakarta.validation.constraints.NotNull;

public record UpgradeSubscriptionRequest(
    @NotNull(message = "plan is required")
    SubscriptionPlan plan
) {}
