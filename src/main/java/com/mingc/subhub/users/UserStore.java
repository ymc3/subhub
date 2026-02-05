package com.mingc.subhub.users;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Component;

@Component
public class UserStore {
  private final AtomicLong seq = new AtomicLong(1000);
  private final ConcurrentMap<Long, User> users = new ConcurrentHashMap<>();

  public User create(String name, String email) {
    long id = seq.incrementAndGet();
    User u = new User(id, name, email);
    users.put(id, u);
    return u;
  }

  public Optional<User> get(long id) {
    return Optional.ofNullable(users.get(id));
  }

  public boolean exists(long id) {
    return users.containsKey(id);
  }
}
