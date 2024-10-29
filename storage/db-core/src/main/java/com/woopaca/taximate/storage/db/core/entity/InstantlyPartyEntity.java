package com.woopaca.taximate.storage.db.core.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Getter;

import java.util.Collections;
import java.util.Set;

@Getter
@Entity(name = "instantly_party")
public class InstantlyPartyEntity extends BaseEntity {

    private String title;

    private int maxParticipants;

    @OneToMany(mappedBy = "party")
    private Set<ParticipationEntity> participationSet = Collections.emptySet();

    public InstantlyPartyEntity() {
    }

    @Builder
    public InstantlyPartyEntity(String title, int maxParticipants) {
        this.title = title;
        this.maxParticipants = maxParticipants;
    }
}
