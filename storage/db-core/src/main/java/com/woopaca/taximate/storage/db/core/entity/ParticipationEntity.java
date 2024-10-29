package com.woopaca.taximate.storage.db.core.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@DynamicUpdate
@Entity(name = "participation")
public class ParticipationEntity extends BaseEntity {

    @Column(columnDefinition = "CHAR(11)")
    private String role;

    @Column(columnDefinition = "CHAR(13)")
    private String status;

    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne(optional = false, targetEntity = UserEntity.class, fetch = FetchType.LAZY)
    private UserEntity user;

    @JoinColumn(name = "party_id", nullable = false)
    @ManyToOne(optional = false, targetEntity = InstantlyPartyEntity.class, fetch = FetchType.LAZY)
    private InstantlyPartyEntity party;

    public ParticipationEntity() {
    }

    @Builder
    public ParticipationEntity(String role, String status, UserEntity user, InstantlyPartyEntity party) {
        this.role = role;
        this.status = status;
        this.user = user;
        this.party = party;
    }

    public static ParticipationEntity participant(InstantlyPartyEntity partyEntity, UserEntity userEntity) {
        return ParticipationEntity.builder()
                .role("PARTICIPANT")
                .user(userEntity)
                .party(partyEntity)
                .status("PARTICIPATING")
                .build();
    }

    public static ParticipationEntity host(InstantlyPartyEntity partyEntity, UserEntity userEntity) {
        return ParticipationEntity.builder()
                .role("HOST")
                .user(userEntity)
                .party(partyEntity)
                .status("PARTICIPATING")
                .build();
    }

    public void leave() {
        this.status = "LEFT";
    }

    public void participate() {
        this.status = "PARTICIPATING";
    }

    public void changeToParticipant() {
        this.role = "PARTICIPANT";
    }

    public void changeToHost() {
        this.role = "HOST";
    }
}
