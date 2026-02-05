package com.mingc.subhub.subscriptions;

import com.mingc.subhub.users.UserStore;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SubscriptionController {
  private final SubscriptionStore subscriptionStore;
  private final UserStore userStore;

  public SubscriptionController(SubscriptionStore subscriptionStore, UserStore userStore) {
    this.subscriptionStore = subscriptionStore;
    this.userStore = userStore;
  }

  @PostMapping("/subscriptions")
  @ResponseStatus(HttpStatus.CREATED)
  public Subscription create(@Valid @RequestBody CreateSubscriptionRequest req) {
    if (!userStore.exists(req.userId())) {
      throw new IllegalArgumentException("user not found: " + req.userId());
    }
    return subscriptionStore.create(req.userId(), req.plan());
  }

  @GetMapping("/subscriptions/{id}")
  public Subscription get(@PathVariable long id) {
    return subscriptionStore.get(id)
        .orElseThrow(() -> new IllegalArgumentException("subscription not found: " + id));
  }
}
