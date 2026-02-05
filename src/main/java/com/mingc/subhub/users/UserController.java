package com.mingc.subhub.users;

import java.time.Instant;

import com.mingc.subhub.users.persistence.UserEntity;
import com.mingc.subhub.users.persistence.UserRepository;

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
  private final UserRepository userRepository;

  public UserController(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @PostMapping("/users")
  @ResponseStatus(HttpStatus.CREATED)
  public User create(@Valid @RequestBody CreateUserRequest req) {
    // simple uniqueness check for nicer error message than raw constraint violation
    userRepository.findByEmail(req.email()).ifPresent(u -> {
      throw new IllegalArgumentException("email already exists");
    });

    UserEntity e = new UserEntity();
    e.setName(req.name());
    e.setEmail(req.email());
    e.setCreatedAt(Instant.now());

    UserEntity saved = userRepository.save(e);
    return new User(saved.getId(), saved.getName(), saved.getEmail());
  }

  @GetMapping("/users/{id}")
  public User get(@PathVariable long id) {
    UserEntity e = userRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("user not found: " + id));
    return new User(e.getId(), e.getName(), e.getEmail());
  }
}
