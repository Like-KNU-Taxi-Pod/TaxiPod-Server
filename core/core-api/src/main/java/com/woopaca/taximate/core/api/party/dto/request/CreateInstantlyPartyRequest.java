package com.woopaca.taximate.core.api.party.dto.request;

import com.woopaca.taximate.core.domain.party.Party;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record CreateInstantlyPartyRequest(@NotBlank String title, @Positive int maxParticipants) {
    public Party toDomain() {
        return Party.builder()
                .title(this.title)
                .maxParticipants(this.maxParticipants)
                .build();
    }
}
