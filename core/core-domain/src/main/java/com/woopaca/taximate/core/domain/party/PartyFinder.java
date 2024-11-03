package com.woopaca.taximate.core.domain.party;

import com.woopaca.taximate.core.domain.error.exception.NonexistentPartyException;
import com.woopaca.taximate.core.domain.user.User;
import com.woopaca.taximate.storage.db.core.entity.PartyEntity;
import com.woopaca.taximate.storage.db.core.repository.PartyRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PartyFinder {

    private final PartyRepository partyRepository;

    public PartyFinder(PartyRepository partyRepository) {
        this.partyRepository = partyRepository;
    }

    public Party findParty(Long partyId) {
        PartyEntity partyEntity = partyRepository.findByIdWithParticipation(partyId)
                .orElseThrow(() -> new NonexistentPartyException(partyId));
        return Party.fromEntity(partyEntity);
    }

    public Party findPartyWithLock(Long partyId) {
        PartyEntity partyEntity = partyRepository.findByIdForUpdate(partyId)
                .orElseThrow(() -> new NonexistentPartyException(partyId));
        return Party.fromEntity(partyEntity);
    }

    public List<Party> findParticipatingParties(User user) {
        return partyRepository.findByParticipationUserId(user.getId())
                .stream()
                .map(Party::fromEntity)
                .toList();
    }
}
