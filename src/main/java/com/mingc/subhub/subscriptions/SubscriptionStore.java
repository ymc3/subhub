package com.mingc.subhub.subscriptions;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Component;

@Component
public class SubscriptionStore {
  private final AtomicLong seq = new AtomicLong(5000);
  private final ConcurrentMap<Long, Subscription> subs = new ConcurrentHashMap<>();

  public Subscription create(long userId, String plan) {
    long id = seq.incrementAndGet();
    Subscription s = new Subscription(id, userId, plan, SubscriptionStatus.TRIAL);
    subs.put(id, s);
    return s;
  }

  public Optional<Subscription> get(long id) {
    return Optional.ofNullable(subs.get(id));
  }
}
