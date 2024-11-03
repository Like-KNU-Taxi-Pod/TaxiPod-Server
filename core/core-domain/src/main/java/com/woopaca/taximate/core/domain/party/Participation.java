package com.woopaca.taximate.core.domain.party;

import com.woopaca.taximate.core.domain.user.User;
import com.woopaca.taximate.storage.db.core.entity.ParticipationEntity;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Participation {

    public static final int MAX_PARTICIPATING_PARTIES_COUNT = 3;

    @EqualsAndHashCode.Include
    private Long id;
    private ParticipationRole role;
    private ParticipationStatus status;
    private LocalDateTime participatedAt;
    private User user;
    private LocalDateTime modifiedAt;

    @Builder
    public Participation(Long id, ParticipationRole role, ParticipationStatus status, LocalDateTime participatedAt, User user, LocalDateTime modifiedAt) {
        this.id = id;
        this.role = role;
        this.status = status;
        this.participatedAt = participatedAt;
        this.user = user;
        this.modifiedAt = modifiedAt;
    }

    public static Participation fromEntity(ParticipationEntity entity) {
        return Participation.builder()
                .id(entity.getId())
                .role(ParticipationRole.valueOf(entity.getRole()))
                .status(ParticipationStatus.valueOf(entity.getStatus()))
                .participatedAt(entity.getCreatedAt())
                .user(User.fromEntity(entity.getUser()))
                .modifiedAt(entity.getUpdatedAt())
                .build();
    }

    public boolean isHost() {
        return role == ParticipationRole.HOST;
    }

    public boolean isParticipating() {
        return status == ParticipationStatus.PARTICIPATING;
    }

    public enum ParticipationRole {

        HOST,
        PARTICIPANT
    }

    public enum ParticipationStatus {

        NONE,
        PARTICIPATING,
        TERMINATED,
        LEFT,
        WARNED,
        BANNED
    }
}
