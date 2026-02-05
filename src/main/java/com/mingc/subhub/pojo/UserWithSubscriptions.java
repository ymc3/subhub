package com.mingc.subhub.pojo;

import java.util.List;

import com.mingc.subhub.pojo.Subscription;

public record UserWithSubscriptions(
    User user,
    List<Subscription> subscriptions
) {}
