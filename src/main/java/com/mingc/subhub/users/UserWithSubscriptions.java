package com.mingc.subhub.users;

import java.util.List;

import com.mingc.subhub.subscriptions.Subscription;

public record UserWithSubscriptions(
    User user,
    List<Subscription> subscriptions
) {}
