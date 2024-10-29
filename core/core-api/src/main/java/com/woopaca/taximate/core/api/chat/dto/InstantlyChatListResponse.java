package com.woopaca.taximate.core.api.chat.dto;

import com.woopaca.taximate.core.api.chat.dto.ChatListResponse.ChatResponse;
import com.woopaca.taximate.core.domain.chat.Chat;
import com.woopaca.taximate.core.domain.party.Party;

import java.util.List;

public record InstantlyChatListResponse(PartyResponse party, List<ChatResponse> chats) {

    public static InstantlyChatListResponse of(Party party, List<Chat> chats) {
        List<ChatResponse> chatResponses = chats.stream()
                .map(ChatResponse::from)
                .toList();
        PartyResponse partyResponse = PartyResponse.from(party);
        return new InstantlyChatListResponse(partyResponse, chatResponses);
    }

    record PartyResponse(Long id, String title) {
        public static PartyResponse from(Party party) {
            return new PartyResponse(party.getId(), party.getTitle());
        }
    }
}
