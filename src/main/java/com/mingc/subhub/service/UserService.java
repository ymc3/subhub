package com.mingc.subhub.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mingc.subhub.pojo.Subscription;
import com.mingc.subhub.pojo.User;
import com.mingc.subhub.pojo.UserWithSubscriptions;
import com.mingc.subhub.repository.SubscriptionRepository;
import com.mingc.subhub.repository.UserEntity;
import com.mingc.subhub.repository.UserRepository;

@Service
public class UserService {
  private final UserRepository userRepository;
  private final SubscriptionRepository subscriptionRepository;

  public UserService(UserRepository userRepository, SubscriptionRepository subscriptionRepository) {
    this.userRepository = userRepository;
    this.subscriptionRepository = subscriptionRepository;
  }

  public User createUser(String name, String email) {
    userRepository.findByEmail(email).ifPresent(u -> {
      throw new IllegalArgumentException("email already exists");
    });

    UserEntity e = new UserEntity();
    e.setName(name);
    e.setEmail(email);

    UserEntity saved = userRepository.save(e);
    return toPojo(saved);
  }

  public User getUser(long id) {
    UserEntity e = userRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("user not found: " + id));
    return toPojo(e);
  }

  public UserWithSubscriptions getUserWithSubscriptions(long id) {
    UserEntity u = userRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("user not found: " + id));

    List<Subscription> subs = subscriptionRepository.findByUser_IdOrderByIdAsc(u.getId())
        .stream()
        .map(UserService::toPojo)
        .toList();

    return new UserWithSubscriptions(toPojo(u), subs);
  }

  private static User toPojo(UserEntity e) {
    return new User(e.getId(), e.getName(), e.getEmail(), e.getCreatedAt(), e.getUpdatedAt());
  }

  private static Subscription toPojo(com.mingc.subhub.repository.SubscriptionEntity e) {
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
