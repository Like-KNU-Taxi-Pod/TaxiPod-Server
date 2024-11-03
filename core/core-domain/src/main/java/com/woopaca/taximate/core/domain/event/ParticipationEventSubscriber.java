package com.woopaca.taximate.core.domain.event;

import com.woopaca.taximate.core.domain.chat.Chat;
import com.woopaca.taximate.core.domain.chat.MessageNotifier;
import com.woopaca.taximate.core.domain.event.dto.LeaveEvent;
import com.woopaca.taximate.core.domain.event.dto.ParticipateEvent;
import com.woopaca.taximate.core.domain.party.Party;
import com.woopaca.taximate.core.domain.user.User;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class ParticipationEventSubscriber {

    private final ChatEventPublisher chatEventPublisher;
    private final MessageNotifier messageNotifier;

    public ParticipationEventSubscriber(ChatEventPublisher chatEventPublisher, MessageNotifier messageNotifier) {
        this.chatEventPublisher = chatEventPublisher;
        this.messageNotifier = messageNotifier;
    }

    @Async("eventHandlerTaskExecutor")
    @TransactionalEventListener
    public void handleParticipateEvent(ParticipateEvent participateEvent) {
        User participant = participateEvent.participant();
        Party party = participateEvent.party();
        Chat participateMessage = Chat.participateMessage(party, participant, participateEvent.participatedAt());
        chatEventPublisher.publishChatEvent(participateMessage);
        messageNotifier.notify(participateMessage);
    }

    @Async("eventHandlerTaskExecutor")
    @TransactionalEventListener
    public void handleLeaveEvent(LeaveEvent leaveEvent) {
        User leaver = leaveEvent.leaver();
        Party party = leaveEvent.party();
        Chat leaveMessage = Chat.leaveMessage(party, leaver, leaveEvent.leftAt());
        chatEventPublisher.publishChatEvent(leaveMessage);
        messageNotifier.notify(leaveMessage);
    }
}
