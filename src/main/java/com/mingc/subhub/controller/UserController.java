package com.mingc.subhub.controller;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.mingc.subhub.pojo.CreateUserRequest;
import com.mingc.subhub.pojo.User;
import com.mingc.subhub.pojo.UserWithSubscriptions;
import com.mingc.subhub.service.UserService;

@RestController
public class UserController {
  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/users")
  @ResponseStatus(HttpStatus.CREATED)
  public User create(@Valid @RequestBody CreateUserRequest req) {
    return userService.createUser(req.name(), req.email());
  }

  @GetMapping("/users/{id}")
  public User get(@PathVariable long id) {
    return userService.getUser(id);
  }

  @GetMapping("/users/{id}/with-subscriptions")
  public UserWithSubscriptions getWithSubscriptions(@PathVariable long id) {
    return userService.getUserWithSubscriptions(id);
  }
}
