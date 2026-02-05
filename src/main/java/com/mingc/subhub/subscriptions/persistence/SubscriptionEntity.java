package com.mingc.subhub.subscriptions.persistence;

import java.time.Instant;

import com.mingc.subhub.subscriptions.SubscriptionStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "subscriptions")
public class SubscriptionEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional = false)
  @JoinColumn(name = "user_id", nullable = false)
  private com.mingc.subhub.users.persistence.UserEntity user;

  @Column(nullable = false)
  private String plan;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private SubscriptionStatus status;

  @Column(name = "created_at", nullable = false)
  private Instant createdAt;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public com.mingc.subhub.users.persistence.UserEntity getUser() {
    return user;
  }

  public void setUser(com.mingc.subhub.users.persistence.UserEntity user) {
    this.user = user;
  }

  public String getPlan() {
    return plan;
  }

  public void setPlan(String plan) {
    this.plan = plan;
  }

  public SubscriptionStatus getStatus() {
    return status;
  }

  public void setStatus(SubscriptionStatus status) {
    this.status = status;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Instant createdAt) {
    this.createdAt = createdAt;
  }
}
