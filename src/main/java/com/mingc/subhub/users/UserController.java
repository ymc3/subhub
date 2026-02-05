package com.mingc.subhub.users;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
  private final UserStore userStore;

  public UserController(UserStore userStore) {
    this.userStore = userStore;
  }

  @PostMapping("/users")
  @ResponseStatus(HttpStatus.CREATED)
  public User create(@Valid @RequestBody CreateUserRequest req) {
    return userStore.create(req.name(), req.email());
  }

  @GetMapping("/users/{id}")
  public User get(@PathVariable long id) {
    return userStore.get(id)
        .orElseThrow(() -> new IllegalArgumentException("user not found: " + id));
  }
}
