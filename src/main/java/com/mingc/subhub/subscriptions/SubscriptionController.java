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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.mingc.subhub.common.PagedResponse;

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

    SubscriptionEntity saved = subscriptionRepository.save(e);
    return new Subscription(
        saved.getId(),
        saved.getUser().getId(),
        saved.getPlan(),
        saved.getStatus(),
        saved.getCreatedAt(),
        saved.getUpdatedAt()
    );
  }

  @GetMapping("/subscriptions/{id}")
  public Subscription get(@PathVariable long id) {
    SubscriptionEntity e = subscriptionRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("subscription not found: " + id));
    return new Subscription(
        e.getId(),
        e.getUser().getId(),
        e.getPlan(),
        e.getStatus(),
        e.getCreatedAt(),
        e.getUpdatedAt()
    );
  }

  @GetMapping("/subscriptions")
  public PagedResponse<Subscription> list(
      @RequestParam(required = false) Long userId,
      @RequestParam(required = false) SubscriptionStatus status,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size
  ) {
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

    var items = entities.getContent().stream()
        .map(e -> new Subscription(
            e.getId(),
            e.getUser().getId(),
            e.getPlan(),
            e.getStatus(),
            e.getCreatedAt(),
            e.getUpdatedAt()
        ))
        .toList();

    return new PagedResponse<>(
        items,
        entities.getNumber(),
        entities.getSize(),
        entities.getTotalElements(),
        entities.getTotalPages()
    );
  }

  @PutMapping("/subscriptions/{id}")
  public Subscription updateStatus(@PathVariable long id, @Valid @RequestBody UpdateSubscriptionStatusRequest req) {
    return setStatus(id, req.status());
  }

  @PostMapping("/subscriptions/{id}/cancel")
  public Subscription cancel(@PathVariable long id) {
    return setStatus(id, SubscriptionStatus.CANCELED);
  }

  @PostMapping("/subscriptions/{id}/activate")
  public Subscription activate(@PathVariable long id) {
    return setStatus(id, SubscriptionStatus.ACTIVE);
  }

  private Subscription setStatus(long id, SubscriptionStatus next) {
    SubscriptionEntity e = subscriptionRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("subscription not found: " + id));

    // Minimal state machine rules (can be expanded later)
    SubscriptionStatus current = e.getStatus();

    if (current == SubscriptionStatus.CANCELED && next != SubscriptionStatus.CANCELED) {
      throw new IllegalArgumentException("cannot transition from CANCELED to " + next);
    }

    e.setStatus(next);
    SubscriptionEntity saved = subscriptionRepository.save(e);
    return new Subscription(
        saved.getId(),
        saved.getUser().getId(),
        saved.getPlan(),
        saved.getStatus(),
        saved.getCreatedAt(),
        saved.getUpdatedAt()
    );
  }
}
