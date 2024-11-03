package com.woopaca.taximate.core.domain.event;

import com.woopaca.taximate.core.domain.event.dto.LeaveEvent;
import com.woopaca.taximate.core.domain.event.dto.ParticipateEvent;
import com.woopaca.taximate.core.domain.party.Party;
import com.woopaca.taximate.core.domain.user.User;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ParticipationEventPublisher {

    private final ApplicationEventPublisher eventPublisher;

    public ParticipationEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public void publishParticipateEvent(Party party, User user, LocalDateTime participatedAt) {
        ParticipateEvent event = new ParticipateEvent(party, user, participatedAt);
        eventPublisher.publishEvent(event);
    }

    public void publishLeaveEvent(Party party, User user, LocalDateTime leftAt) {
        LeaveEvent event = new LeaveEvent(party, user, leftAt);
        eventPublisher.publishEvent(event);
    }
}
