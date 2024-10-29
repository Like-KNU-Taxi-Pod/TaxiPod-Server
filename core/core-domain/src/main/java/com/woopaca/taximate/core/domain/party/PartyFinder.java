package com.woopaca.taximate.core.domain.party;

import com.woopaca.taximate.core.domain.error.exception.NonexistentPartyException;
import com.woopaca.taximate.core.domain.user.User;
import com.woopaca.taximate.storage.db.core.entity.InstantlyPartyEntity;
import com.woopaca.taximate.storage.db.core.entity.PartyEntity;
import com.woopaca.taximate.storage.db.core.repository.InstantlyPartyRepository;
import com.woopaca.taximate.storage.db.core.repository.PartyRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PartyFinder {

    private final PartyRepository partyRepository;
    private final InstantlyPartyRepository instantlyPartyRepository;

    public PartyFinder(PartyRepository partyRepository, InstantlyPartyRepository instantlyPartyRepository) {
        this.partyRepository = partyRepository;
        this.instantlyPartyRepository = instantlyPartyRepository;
    }

    public Party findParty(Long partyId) {
        InstantlyPartyEntity partyEntity = instantlyPartyRepository.findByIdWithParticipation(partyId)
                .orElseThrow(() -> new NonexistentPartyException(partyId));
        return Party.fromEntity(partyEntity);
    }

    public Party findPartyWithLock(Long partyId) {
        InstantlyPartyEntity partyEntity = instantlyPartyRepository.findByIdForUpdate(partyId)
                .orElseThrow(() -> new NonexistentPartyException(partyId));
        return Party.fromEntity(partyEntity);
    }

    public List<Party> findParticipatingParties(User user) {
        return instantlyPartyRepository.findByParticipationUserId(user.getId())
                .stream()
                .map(Party::fromEntity)
                .toList();
    }
}
