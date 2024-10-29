package com.woopaca.taximate.core.domain.party;

import com.woopaca.taximate.storage.db.core.repository.InstantlyPartyRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class InstantlyPartyFinder {

    private final InstantlyPartyRepository instantlyPartyRepository;

    public InstantlyPartyFinder(InstantlyPartyRepository instantlyPartyRepository) {
        this.instantlyPartyRepository = instantlyPartyRepository;
    }

    public List<Party> findRecentParties(Duration recentDuration) {
        Sort sortByCreatedAtDesc = Sort.by(Sort.Order.desc("createdAt"));
        LocalDateTime recentThreshold = LocalDateTime.now()
                .minus(recentDuration);
        return instantlyPartyRepository.findByCreatedAtAfter(recentThreshold, sortByCreatedAtDesc)
                .stream()
                .map(Party::fromEntity)
                .toList();
    }
}
