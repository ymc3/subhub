package com.mingc.subhub.controller;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.mingc.subhub.common.PagedResponse;
import com.mingc.subhub.pojo.CreateSubscriptionRequest;
import com.mingc.subhub.pojo.Subscription;
import com.mingc.subhub.pojo.SubscriptionStatus;
import com.mingc.subhub.pojo.UpdateSubscriptionStatusRequest;
import com.mingc.subhub.service.SubscriptionService;

@RestController
public class SubscriptionController {
  private final SubscriptionService subscriptionService;

  public SubscriptionController(SubscriptionService subscriptionService) {
    this.subscriptionService = subscriptionService;
  }

  @PostMapping("/subscriptions")
  @ResponseStatus(HttpStatus.CREATED)
  public Subscription create(@Valid @RequestBody CreateSubscriptionRequest req) {
    return subscriptionService.create(req.userId(), req.plan());
  }

  @GetMapping("/subscriptions/{id}")
  public Subscription get(@PathVariable long id) {
    return subscriptionService.get(id);
  }

  @GetMapping("/subscriptions")
  public PagedResponse<Subscription> list(
      @RequestParam(required = false) Long userId,
      @RequestParam(required = false) SubscriptionStatus status,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size
  ) {
    return subscriptionService.list(userId, status, page, size);
  }

  @PutMapping("/subscriptions/{id}")
  public Subscription updateStatus(@PathVariable long id, @Valid @RequestBody UpdateSubscriptionStatusRequest req) {
    return subscriptionService.updateStatus(id, req.status());
  }

  @PostMapping("/subscriptions/{id}/cancel")
  public Subscription cancel(@PathVariable long id) {
    return subscriptionService.cancel(id);
  }

  @PostMapping("/subscriptions/{id}/activate")
  public Subscription activate(@PathVariable long id) {
    return subscriptionService.activate(id);
  }
}
