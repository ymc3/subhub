package com.mingc.subhub.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.mingc.subhub.pojo.SubscriptionStatus;

public interface SubscriptionRepository extends JpaRepository<SubscriptionEntity, Long> {
  Optional<SubscriptionEntity> findTopByUser_IdAndStatusInOrderByUpdatedAtDesc(Long userId, Collection<SubscriptionStatus> statuses);

  List<SubscriptionEntity> findByUser_IdOrderByIdAsc(Long userId);

  Page<SubscriptionEntity> findByUser_Id(Long userId, Pageable pageable);

  Page<SubscriptionEntity> findByStatus(SubscriptionStatus status, Pageable pageable);

  Page<SubscriptionEntity> findByUser_IdAndStatus(Long userId, SubscriptionStatus status, Pageable pageable);
}
