package com.woopaca.taximate.core.domain.party;

import com.woopaca.taximate.storage.db.core.entity.PartyEntity;
import com.woopaca.taximate.storage.db.core.repository.PartyRepository;
import org.springframework.stereotype.Component;

@Component
public class PartyAppender {

    private final PartyRepository partyRepository;

    public PartyAppender(PartyRepository partyRepository) {
        this.partyRepository = partyRepository;
    }

    public Party appendNew(Party party) {
        PartyEntity partyEntity = PartyEntity.builder()
                .title(party.getTitle())
                .explanation(party.getExplanation())
                .departureTime(party.getDepartureTime())
                .origin(party.getOrigin())
                .originAddress(party.getOriginAddress())
                .originLatitude(party.getOriginLocation().latitude())
                .originLongitude(party.getOriginLocation().longitude())
                .destination(party.getDestination())
                .destinationAddress(party.getDestinationAddress())
                .destinationLatitude(party.getDestinationLocation().latitude())
                .destinationLongitude(party.getDestinationLocation().longitude())
                .maxParticipants(party.getMaxParticipants())
                .build();
        PartyEntity savedPartyEntity = partyRepository.save(partyEntity);
        return Party.fromEntity(savedPartyEntity);
    }
}
