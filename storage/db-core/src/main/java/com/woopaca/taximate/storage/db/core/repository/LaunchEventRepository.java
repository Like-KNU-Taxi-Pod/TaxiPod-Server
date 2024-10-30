package com.woopaca.taximate.storage.db.core.repository;

import com.woopaca.taximate.storage.db.core.entity.LaunchEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LaunchEventRepository extends JpaRepository<LaunchEventEntity, Long> {

    Optional<LaunchEventEntity> findByUserId(Long userId);
}
