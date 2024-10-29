package com.woopaca.taximate.storage.db.core.repository;

import com.woopaca.taximate.storage.db.core.entity.InstantlyPartyEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface InstantlyPartyRepository extends JpaRepository<InstantlyPartyEntity, Long> {

    @EntityGraph(attributePaths = {"participationSet", "participationSet.user"})
    List<InstantlyPartyEntity> findByCreatedAtAfter(LocalDateTime createdAt, Sort sort);
}
