package com.woopaca.taximate.storage.db.core.repository;

import com.woopaca.taximate.storage.db.core.entity.InstantlyPartyEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface InstantlyPartyRepository extends JpaRepository<InstantlyPartyEntity, Long> {

    @EntityGraph(attributePaths = {"participationSet", "participationSet.user"})
    List<InstantlyPartyEntity> findByCreatedAtAfter(LocalDateTime createdAt, Sort sort);

    @EntityGraph(attributePaths = {"participationSet", "participationSet.user"})
    @Query("""
            SELECT p
            FROM instantly_party p
            WHERE p.id = :id
            """)
    Optional<InstantlyPartyEntity> findByIdWithParticipation(@Param("id") Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            SELECT p
            FROM instantly_party p
            WHERE p.id = :id
            """)
    Optional<InstantlyPartyEntity> findByIdForUpdate(@Param("id") Long id);

    @EntityGraph(attributePaths = {"participationSet", "participationSet.user"})
    @Query("""
            SELECT p
            FROM instantly_party p
            WHERE p.id IN (
                    SELECT pt.party.id
                    FROM participation pt
                    WHERE pt.user.id = :userId AND pt.status = 'PARTICIPATING'
            )
            """)
    List<InstantlyPartyEntity> findByParticipationUserId(@Param("userId") Long userId);
}
