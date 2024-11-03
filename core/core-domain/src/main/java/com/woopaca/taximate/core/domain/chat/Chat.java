package com.woopaca.taximate.core.domain.chat;

import com.woopaca.taximate.core.domain.chat.id.IdGenerator;
import com.woopaca.taximate.core.domain.error.exception.ChatMessageTooLongException;
import com.woopaca.taximate.core.domain.party.Party;
import com.woopaca.taximate.core.domain.user.User;
import com.woopaca.taximate.storage.db.core.entity.ChatEntity;
import com.woopaca.taximate.storage.db.core.entity.UserEntity;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Chat {

    public static final int MAX_MESSAGE_LENGTH = 500;
    public static final String PARTICIPATE_MESSAGE = "%s님이 팟에 참여했습니다.";
    public static final String LEAVE_MESSAGE = "%s님이 팟을 나갔습니다.";

    @EqualsAndHashCode.Include
    private Long id;
    private String message;
    private MessageType type;
    private LocalDateTime sentAt;
    private User sender;
    private Party party;

    @Builder
    public Chat(Long id, String message, MessageType type, LocalDateTime sentAt, User sender, Party party) {
        if (message.length() > MAX_MESSAGE_LENGTH) {
            throw new ChatMessageTooLongException();
        }

        this.id = id;
        this.message = message.trim();
        this.type = type;
        this.sentAt = sentAt;
        this.sender = sender;
        this.party = party;
    }

    public static Chat standardMessage(Party party, User sender, String message) {
        return Chat.builder()
                .id(IdGenerator.generateId())
                .message(message)
                .type(MessageType.MESSAGE)
                .sentAt(LocalDateTime.now())
                .sender(sender)
                .party(party)
                .build();
    }

    public static Chat participateMessage(Party party, User participant, LocalDateTime participatedAt) {
        return systemMessage(party, participant, String.format(PARTICIPATE_MESSAGE, participant.getNickname()), participatedAt);
    }

    public static Chat leaveMessage(Party party, User leaver, LocalDateTime leftAt) {
        return systemMessage(party, leaver, String.format(LEAVE_MESSAGE, leaver.getNickname()), leftAt);
    }

    private static Chat systemMessage(Party party, User sender, String message, LocalDateTime sentAt) {
        return Chat.builder()
                .id(IdGenerator.generateId())
                .message(message)
                .type(MessageType.SYSTEM)
                .sentAt(sentAt)
                .sender(sender)
                .party(party)
                .build();
    }

    public static Chat empty(Party party) {
        return Chat.builder()
                .message("")
                .type(MessageType.MESSAGE)
                .sentAt(LocalDateTime.MIN)
                .party(party)
                .build();
    }

    public static Chat fromEntity(ChatEntity entity, Party party) {
        return Chat.builder()
                .id(entity.getId())
                .message(entity.getMessage())
                .type(MessageType.valueOf(entity.getType()))
                .sentAt(entity.getCreatedAt())
                .party(party)
                .sender(User.fromEntity(entity.getUser()))
                .build();
    }

    public ChatEntity toEntity(UserEntity user) {
        return ChatEntity.builder()
                .id(id)
                .message(message)
                .type(type.name())
                .partyId(party.getId())
                .createdAt(sentAt)
                .user(user)
                .build();
    }

    public boolean isParticipateMessage() {
        return type.equals(MessageType.SYSTEM) && message.contains(String.format(PARTICIPATE_MESSAGE, ""));
    }

    public boolean isLeaveMessage() {
        return type.equals(MessageType.SYSTEM) && message.contains(String.format(LEAVE_MESSAGE, ""));
    }
}
