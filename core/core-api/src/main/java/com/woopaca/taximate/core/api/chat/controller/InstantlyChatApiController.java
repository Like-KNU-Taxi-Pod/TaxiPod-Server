package com.woopaca.taximate.core.api.chat.controller;

import com.woopaca.taximate.core.api.chat.dto.InstantlyChatListResponse;
import com.woopaca.taximate.core.api.common.model.ApiResults;
import com.woopaca.taximate.core.domain.chat.Chat;
import com.woopaca.taximate.core.domain.chat.service.ChatService;
import com.woopaca.taximate.core.domain.party.Party;
import com.woopaca.taximate.core.domain.party.PartyFinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/v2/chats")
@RestController
public class InstantlyChatApiController {

    private final PartyFinder partyFinder;
    private final ChatService chatService;

    public InstantlyChatApiController(PartyFinder partyFinder, ChatService chatService) {
        this.partyFinder = partyFinder;
        this.chatService = chatService;
    }

    @GetMapping("/{partyId}")
    public ApiResults.ApiResponse<InstantlyChatListResponse> chatList(@PathVariable("partyId") Long partyId) {
        Party party = partyFinder.findParty(partyId);
        List<Chat> chats = chatService.getChats(party);
        InstantlyChatListResponse response = InstantlyChatListResponse.of(party, chats);
        return ApiResults.success(response);
    }
}
