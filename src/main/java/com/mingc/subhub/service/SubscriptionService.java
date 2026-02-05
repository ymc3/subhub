package com.mingc.subhub.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.mingc.subhub.common.PagedResponse;
import com.mingc.subhub.pojo.Subscription;
import com.mingc.subhub.pojo.SubscriptionStatus;
import com.mingc.subhub.repository.SubscriptionEntity;
import com.mingc.subhub.repository.SubscriptionRepository;
import com.mingc.subhub.repository.UserEntity;
import com.mingc.subhub.repository.UserRepository;

@Service
public class SubscriptionService {
  private final SubscriptionRepository subscriptionRepository;
  private final UserRepository userRepository;

  public SubscriptionService(SubscriptionRepository subscriptionRepository, UserRepository userRepository) {
    this.subscriptionRepository = subscriptionRepository;
    this.userRepository = userRepository;
  }

  public Subscription create(long userId, String plan) {
    UserEntity user = userRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException("user not found: " + userId));

    SubscriptionEntity e = new SubscriptionEntity();
    e.setUser(user);
    e.setPlan(plan);
    e.setStatus(SubscriptionStatus.TRIAL);

    SubscriptionEntity saved = subscriptionRepository.save(e);
    return toPojo(saved);
  }

  public Subscription get(long id) {
    SubscriptionEntity e = subscriptionRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("subscription not found: " + id));
    return toPojo(e);
  }

  public PagedResponse<Subscription> list(Long userId, SubscriptionStatus status, int page, int size) {
    if (page < 0) throw new IllegalArgumentException("page must be >= 0");
    if (size < 1 || size > 100) throw new IllegalArgumentException("size must be between 1 and 100");

    var pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id"));

    var entities = subscriptionRepository.findAll(pageable);
    if (userId != null && status != null) {
      entities = subscriptionRepository.findByUser_IdAndStatus(userId, status, pageable);
    } else if (userId != null) {
      entities = subscriptionRepository.findByUser_Id(userId, pageable);
    } else if (status != null) {
      entities = subscriptionRepository.findByStatus(status, pageable);
    }

    var items = entities.getContent().stream().map(SubscriptionService::toPojo).toList();

    return new PagedResponse<>(
        items,
        entities.getNumber(),
        entities.getSize(),
        entities.getTotalElements(),
        entities.getTotalPages()
    );
  }

  public Subscription updateStatus(long id, SubscriptionStatus next) {
    return setStatus(id, next);
  }

  public Subscription cancel(long id) {
    return setStatus(id, SubscriptionStatus.CANCELED);
  }

  public Subscription activate(long id) {
    return setStatus(id, SubscriptionStatus.ACTIVE);
  }

  private Subscription setStatus(long id, SubscriptionStatus next) {
    SubscriptionEntity e = subscriptionRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("subscription not found: " + id));

    SubscriptionStatus current = e.getStatus();
    if (current == SubscriptionStatus.CANCELED && next != SubscriptionStatus.CANCELED) {
      throw new IllegalArgumentException("cannot transition from CANCELED to " + next);
    }

    e.setStatus(next);
    SubscriptionEntity saved = subscriptionRepository.save(e);
    return toPojo(saved);
  }

  private static Subscription toPojo(SubscriptionEntity e) {
    return new Subscription(
        e.getId(),
        e.getUser().getId(),
        e.getPlan(),
        e.getStatus(),
        e.getCreatedAt(),
        e.getUpdatedAt()
    );
  }
}
