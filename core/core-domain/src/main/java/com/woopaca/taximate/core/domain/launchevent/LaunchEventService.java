package com.woopaca.taximate.core.domain.launchevent;

import com.woopaca.taximate.core.domain.auth.AuthenticatedUserHolder;
import com.woopaca.taximate.core.domain.user.User;
import com.woopaca.taximate.storage.db.core.entity.LaunchEventEntity;
import com.woopaca.taximate.storage.db.core.repository.LaunchEventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Service
public class LaunchEventService {

    // 2024-11-06 23:59:59
    private static final LocalDateTime EVENT_END_TIME = LocalDateTime.of(2024, 10, 30, 23, 59, 59)
            .plusDays(7);

    private final LaunchEventRepository launchEventRepository;

    public LaunchEventService(LaunchEventRepository launchEventRepository) {
        this.launchEventRepository = launchEventRepository;
    }

    @Transactional
    public void participateLaunchEvent(String phone) {
        validatePhone(phone);
        validateEventPeriod();

        User authenticatedUser = AuthenticatedUserHolder.getAuthenticatedUser();
        launchEventRepository.findByUserId(authenticatedUser.getId())
                .orElseGet(() -> {
                    LaunchEventEntity launchEventEntity = LaunchEventEntity.builder()
                            .userId(authenticatedUser.getId())
                            .phone(phone)
                            .build();
                    return launchEventRepository.save(launchEventEntity);
                });
    }

    private void validateEventPeriod() {
        LocalDateTime currentTime = LocalDateTime.now();
        if (currentTime.isAfter(EVENT_END_TIME)) {
            throw new IllegalStateException("출시 이벤트가 종료되었습니다!");
        }
    }

    private void validatePhone(String phone) {
        if (StringUtils.hasText(phone) && phone.length() != 11) {
            throw new IllegalArgumentException("휴대폰 번호는 11자리여야 합니다.");
        }

        if (!phone.matches("\\d+")) {
            throw new IllegalArgumentException("휴대폰 번호는 숫자만 입력해야 합니다.");
        }
    }
}
