package com.woopaca.taximate.core.domain.party;

import com.woopaca.taximate.core.domain.error.exception.NonexistentPartyException;
import com.woopaca.taximate.core.domain.error.exception.NonexistentUserException;
import com.woopaca.taximate.core.domain.error.exception.NotParticipatedPartyException;
import com.woopaca.taximate.core.domain.user.User;
import com.woopaca.taximate.storage.db.core.entity.InstantlyPartyEntity;
import com.woopaca.taximate.storage.db.core.entity.ParticipationEntity;
import com.woopaca.taximate.storage.db.core.entity.UserEntity;
import com.woopaca.taximate.storage.db.core.repository.InstantlyPartyRepository;
import com.woopaca.taximate.storage.db.core.repository.ParticipationRepository;
import com.woopaca.taximate.storage.db.core.repository.PartyRepository;
import com.woopaca.taximate.storage.db.core.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.Objects;

@Component
public class ParticipationModifier {

    private final ParticipationRepository participationRepository;
    private final PartyRepository partyRepository;
    private final UserRepository userRepository;
    private final InstantlyPartyRepository instantlyPartyRepository;

    public ParticipationModifier(ParticipationRepository participationRepository, PartyRepository partyRepository, UserRepository userRepository, InstantlyPartyRepository instantlyPartyRepository) {
        this.participationRepository = participationRepository;
        this.partyRepository = partyRepository;
        this.userRepository = userRepository;
        this.instantlyPartyRepository = instantlyPartyRepository;
    }

    public Participation appendHost(Party party, User user) {
        UserEntity userEntity = userRepository.findById(user.getId())
                .orElseThrow(NonexistentUserException::new);
        /*PartyEntity partyEntity = partyRepository.findById(party.getId())
                .orElseThrow(() -> new NonexistentPartyException(party.getId()));*/
        InstantlyPartyEntity instantlyPartyEntity = instantlyPartyRepository.findById(party.getId())
                .orElseThrow(() -> new NonexistentPartyException(party.getId()));
        ParticipationEntity participationEntity = ParticipationEntity.host(instantlyPartyEntity, userEntity);
        ParticipationEntity savedParticipationEntity = participationRepository.save(participationEntity);
        return Participation.fromEntity(savedParticipationEntity);
    }

    public void appendParticipant(Party party, User user) {
        participationRepository.findByPartyIdAndUserId(party.getId(), user.getId())
                .ifPresentOrElse(entity -> {
                    entity.participate();
                    participationRepository.save(entity);
                }, () -> {
                    UserEntity userEntity = userRepository.findById(user.getId())
                            .orElseThrow(NonexistentUserException::new);
                    /*PartyEntity partyEntity = partyRepository.findById(party.getId())
                            .orElseThrow(() -> new NonexistentPartyException(party.getId()));*/
                    InstantlyPartyEntity instantlyPartyEntity = instantlyPartyRepository.findById(party.getId())
                            .orElseThrow(() -> new NonexistentPartyException(party.getId()));
                    ParticipationEntity participationEntity = ParticipationEntity
                            .participant(instantlyPartyEntity, userEntity);
                    participationRepository.save(participationEntity);
                });
    }

    public void delegateHost(Party party, User host) {
        if (party.currentParticipantsCount() == 1) {
            return;
        }
        participationRepository.findByPartyIdAndUserId(party.getId(), host.getId())
                .ifPresent(ParticipationEntity::changeToParticipant);
        party.getParticipationSet()
                .stream()
                .filter(participation -> !Objects.equals(participation.getUser(), host))
                .max(Comparator.comparing(Participation::getParticipatedAt))
                .flatMap(participation -> participationRepository
                        .findByPartyIdAndUserId(party.getId(), participation.getUser().getId()))
                .ifPresent(ParticipationEntity::changeToHost);
    }

    public void removeParticipant(Party party, User user) {
        ParticipationEntity participationEntity = participationRepository.findByPartyIdAndUserId(party.getId(), user.getId())
                .orElseThrow(NotParticipatedPartyException::new);
        participationEntity.leave();
    }
}
