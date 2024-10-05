package com.woopaca.taximate.core.domain.chat;

import com.woopaca.taximate.core.domain.party.Party;
import com.woopaca.taximate.core.domain.user.User;
import com.woopaca.taximate.storage.db.core.repository.ChatRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Component;

@Component
public class ChatFinder {

    private final ChatRepository chatRepository;

    public ChatFinder(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    public Chat findRecentMessage(Party party) {
        return chatRepository.findTopByPartyId(party.getId(), Sort.by(Order.desc("id")))
                .map(entity -> Chat.fromEntity(entity, party))
                .orElseGet(() -> Chat.empty(party));
    }

    public int calculateUnreadCount(Party party, User user) {
        return chatRepository.countByLastChatId(party.getId(), user.getId());
    }
}