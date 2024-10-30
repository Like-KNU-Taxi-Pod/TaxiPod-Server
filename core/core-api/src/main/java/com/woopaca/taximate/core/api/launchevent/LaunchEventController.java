package com.woopaca.taximate.core.api.launchevent;

import com.woopaca.taximate.core.api.common.model.ApiResults;
import com.woopaca.taximate.core.api.common.model.ApiResults.ApiResponse;
import com.woopaca.taximate.core.domain.launchevent.LaunchEventService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/events")
@RestController
public class LaunchEventController {

    private final LaunchEventService launchEventService;

    public LaunchEventController(LaunchEventService launchEventService) {
        this.launchEventService = launchEventService;
    }

    @PostMapping
    public ApiResponse<String> participateEvent(@RequestBody String phone) {
        launchEventService.participateLaunchEvent(phone);
        return ApiResults.success("감사합니다! 🙇🏻‍♂️");
    }
}
