package com.woopaca.taximate.core.api.party.dto.response;

import com.woopaca.taximate.core.domain.party.Participation.ParticipationStatus;
import com.woopaca.taximate.core.domain.party.Party;
import com.woopaca.taximate.core.domain.user.User;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record InstantlyPartiesResponse(Long id, String title, int maxParticipants, int currentParticipants,
                                       String status, LocalDateTime createdAt, Host host) {

    public static InstantlyPartiesResponse of(Party party, User authenticatedUser) {
        User host = party.getHost();
        ParticipationStatus participationStatus = party.participationStatusOf(authenticatedUser);
        return InstantlyPartiesResponse.builder()
                .id(party.getId())
                .title(party.getTitle())
                .maxParticipants(party.getMaxParticipants())
                .currentParticipants(party.currentParticipantsCount())
                .status(participationStatus.name())
                .createdAt(party.getCreatedAt())
                .host(Host.from(host))
                .build();
    }

    @Builder
    record Host(Long id, String nickname, String profileImage, boolean isMe) {

        public static Host from(User user) {
            return Host.builder()
                    .id(user.getId())
                    .nickname(user.getNickname())
                    .profileImage(user.getProfileImage())
                    .isMe(user.isCurrentUser())
                    .build();
        }
    }
}
