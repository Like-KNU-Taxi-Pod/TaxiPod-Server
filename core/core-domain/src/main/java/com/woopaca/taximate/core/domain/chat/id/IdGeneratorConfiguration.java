package com.woopaca.taximate.core.domain.chat.id;

import com.woopaca.taximate.storage.db.core.repository.ChatRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

@Configuration
public class IdGeneratorConfiguration {

    private final ChatRepository chatRepository;

    public IdGeneratorConfiguration(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    @PostConstruct
    public void init() {
        chatRepository.findTopBy(Sort.by("id").descending())
                .ifPresent(entity -> IdGenerator.setInitialId(entity.getId()));
    }
}
