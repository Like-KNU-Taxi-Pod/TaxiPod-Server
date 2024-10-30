package com.woopaca.taximate.storage.db.core.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.Getter;

@Getter
@Entity(name = "launch_event")
public class LaunchEventEntity extends BaseEntity {

    @Column(columnDefinition = "CHAR(11)")
    private String phone;

    private Long userId;

    public LaunchEventEntity() {
    }

    @Builder
    public LaunchEventEntity(String phone, Long userId) {
        this.phone = phone;
        this.userId = userId;
    }
}
