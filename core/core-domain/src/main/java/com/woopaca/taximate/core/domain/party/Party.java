package com.woopaca.taximate.core.domain.party;

import com.woopaca.taximate.core.domain.local.model.Address;
import com.woopaca.taximate.core.domain.party.Participation.ParticipationStatus;
import com.woopaca.taximate.core.domain.party.model.Coordinate;
import com.woopaca.taximate.core.domain.user.User;
import com.woopaca.taximate.storage.db.core.entity.InstantlyPartyEntity;
import com.woopaca.taximate.storage.db.core.entity.PartyEntity;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Party {

    public static final int MAX_TITLE_LENGTH = 30;
    public static final int MAX_EXPLANATION_LENGTH = 500;
    public static final int MAX_PARTICIPANTS_COUNT = 4;
    public static final int MIN_PARTICIPANTS_COUNT = 2;

    @EqualsAndHashCode.Include
    private Long id;
    private String title;
    private String explanation;
    private LocalDateTime departureTime;
    private String origin;
    private String originAddress;
    private Coordinate originLocation;
    private String destination;
    private String destinationAddress;
    private Coordinate destinationLocation;
    private int maxParticipants;
    private int views;
    private LocalDateTime createdAt;
    private Set<Participation> participationSet;

    @Builder
    public Party(Long id, String title, String explanation, LocalDateTime departureTime,
                 String origin, String originAddress, Coordinate originLocation,
                 String destination, String destinationAddress, Coordinate destinationLocation,
                 int maxParticipants, int views, LocalDateTime createdAt, Set<Participation> participationSet) {
        this.id = id;
        this.title = title;
        this.explanation = explanation;
        this.departureTime = departureTime;
        this.origin = origin;
        this.originAddress = originAddress;
        this.originLocation = originLocation;
        this.destination = destination;
        this.destinationAddress = destinationAddress;
        this.destinationLocation = destinationLocation;
        this.maxParticipants = maxParticipants;
        this.views = views;
        this.createdAt = createdAt;
        this.participationSet = participationSet;
    }

    public static Party fromEntity(PartyEntity entity) {
        Set<Participation> participationSet = entity.getParticipationSet()
                .stream()
                .map(Participation::fromEntity)
                .collect(Collectors.toSet());
        return defaultBuilder(entity)
                .participationSet(participationSet)
                .build();
    }

    public static Party fromEntity(InstantlyPartyEntity entity) {
        Set<Participation> participationSet = entity.getParticipationSet()
                .stream()
                .map(Participation::fromEntity)
                .collect(Collectors.toSet());
        return Party.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .maxParticipants(entity.getMaxParticipants())
                .createdAt(entity.getCreatedAt())
                .participationSet(participationSet)
                .build();
    }

    public static Party fromEntityExcludeParticipants(PartyEntity entity) {
        return defaultBuilder(entity)
                .build();
    }

    private static PartyBuilder defaultBuilder(PartyEntity entity) {
        return Party.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .explanation(entity.getExplanation())
                .departureTime(entity.getDepartureTime())
                .origin(entity.getOrigin())
                .originAddress(entity.getOriginAddress())
                .originLocation(Coordinate.of(
                        entity.getOriginLatitude(),
                        entity.getOriginLongitude()
                ))
                .destination(entity.getDestination())
                .destinationAddress(entity.getDestinationAddress())
                .destinationLocation(Coordinate.of(
                        entity.getDestinationLatitude(),
                        entity.getDestinationLongitude()
                ))
                .maxParticipants(entity.getMaxParticipants())
                .views(entity.getViews())
                .createdAt(entity.getCreatedAt());
    }

    public int currentParticipantsCount() {
        return (int) participationSet.stream()
                .filter(Participation::isParticipating)
                .count();
    }

    public User getHost() {
        return participationSet.stream()
                .filter(Participation::isHost)
                .findAny()
                .map(Participation::getUser)
                .orElse(null);
    }

    public ParticipationStatus participationStatusOf(User user) {
        if (isTerminated()) {
            return ParticipationStatus.TERMINATED;
        }
        return participationSet.stream()
                .filter(participation -> Objects.equals(participation.getUser(), user))
                .findAny()
                .map(Participation::getStatus)
                .orElse(ParticipationStatus.NONE);
    }

    public boolean isProgress() {
        if (departureTime == null) {
            return true;
        }
        return departureTime.isAfter(LocalDateTime.now().minusMinutes(10));
    }

    public boolean isTerminated() {
        return !isProgress();
    }

    public Party allocateAddress(Address originAddress, Address destinationAddress) {
        this.origin = originAddress.name();
        this.originAddress = originAddress.fullAddress();
        this.destination = destinationAddress.name();
        this.destinationAddress = destinationAddress.fullAddress();
        return this;
    }

    public boolean isParticipated(User user) {
        return participationSet.stream()
                .filter(Participation::isParticipating)
                .anyMatch(participation -> Objects.equals(participation.getUser(), user));
    }

    public boolean isFull() {
        return currentParticipantsCount() >= maxParticipants;
    }

    public LocalDateTime getParticipatedAt(User user) {
        return participationSet.stream()
                .filter(participation -> Objects.equals(participation.getUser(), user))
                .findAny()
                .map(Participation::getParticipatedAt)
                .orElse(LocalDateTime.MIN);
    }

    public boolean isHostUser(User user) {
        return Objects.equals(getHost(), user);
    }

    public List<User> getParticipants() {
        return getParticipationSet().stream()
                .filter(Participation::isParticipating)
                .map(Participation::getUser)
                .toList();
    }
}
