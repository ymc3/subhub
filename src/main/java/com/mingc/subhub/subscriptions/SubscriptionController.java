package com.mingc.subhub.subscriptions;

import java.time.Instant;

import com.mingc.subhub.subscriptions.persistence.SubscriptionEntity;
import com.mingc.subhub.subscriptions.persistence.SubscriptionRepository;
import com.mingc.subhub.users.persistence.UserEntity;
import com.mingc.subhub.users.persistence.UserRepository;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SubscriptionController {
  private final SubscriptionRepository subscriptionRepository;
  private final UserRepository userRepository;

  public SubscriptionController(SubscriptionRepository subscriptionRepository, UserRepository userRepository) {
    this.subscriptionRepository = subscriptionRepository;
    this.userRepository = userRepository;
  }

  @PostMapping("/subscriptions")
  @ResponseStatus(HttpStatus.CREATED)
  public Subscription create(@Valid @RequestBody CreateSubscriptionRequest req) {
    UserEntity user = userRepository.findById(req.userId())
        .orElseThrow(() -> new IllegalArgumentException("user not found: " + req.userId()));

    SubscriptionEntity e = new SubscriptionEntity();
    e.setUser(user);
    e.setPlan(req.plan());
    e.setStatus(SubscriptionStatus.TRIAL);
    e.setCreatedAt(Instant.now());

    SubscriptionEntity saved = subscriptionRepository.save(e);
    return new Subscription(saved.getId(), saved.getUser().getId(), saved.getPlan(), saved.getStatus());
  }

  @GetMapping("/subscriptions/{id}")
  public Subscription get(@PathVariable long id) {
    SubscriptionEntity e = subscriptionRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("subscription not found: " + id));
    return new Subscription(e.getId(), e.getUser().getId(), e.getPlan(), e.getStatus());
  }

  @PutMapping("/subscriptions/{id}")
  public Subscription updateStatus(@PathVariable long id, @Valid @RequestBody UpdateSubscriptionStatusRequest req) {
    SubscriptionEntity e = subscriptionRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("subscription not found: " + id));

    // Minimal state machine rules (can be expanded later)
    SubscriptionStatus current = e.getStatus();
    SubscriptionStatus next = req.status();

    if (current == SubscriptionStatus.CANCELED && next != SubscriptionStatus.CANCELED) {
      throw new IllegalArgumentException("cannot transition from CANCELED to " + next);
    }

    e.setStatus(next);
    SubscriptionEntity saved = subscriptionRepository.save(e);
    return new Subscription(saved.getId(), saved.getUser().getId(), saved.getPlan(), saved.getStatus());
  }
}
