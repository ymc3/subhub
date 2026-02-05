package com.mingc.subhub.subscriptions.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionRepository extends JpaRepository<SubscriptionEntity, Long> {
  List<SubscriptionEntity> findByUser_IdOrderByIdAsc(Long userId);
}
