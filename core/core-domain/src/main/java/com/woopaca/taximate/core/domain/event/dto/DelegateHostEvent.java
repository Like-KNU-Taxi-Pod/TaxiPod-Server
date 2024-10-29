package com.woopaca.taximate.core.domain.event.dto;

import com.woopaca.taximate.core.domain.party.Party;
import com.woopaca.taximate.core.domain.user.User;

import java.time.LocalDateTime;

public record DelegateHostEvent(Party party, User leaver, User newHost, LocalDateTime delegatedAt) {
}
