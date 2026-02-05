package com.mingc.subhub.subscriptions.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionRepository extends JpaRepository<SubscriptionEntity, Long> {
}
