package com.woopaca.taximate.core.api.party.controller;

import com.woopaca.taximate.core.api.common.model.ApiResults;
import com.woopaca.taximate.core.api.common.model.ApiResults.ApiResponse;
import com.woopaca.taximate.core.api.party.dto.response.InstantlyPartiesResponse;
import com.woopaca.taximate.core.domain.party.InstantlyParties;
import com.woopaca.taximate.core.domain.party.service.PartyService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/v2/parties")
@RestController
public class InstantlyPartyController {

    private final PartyService partyService;

    public InstantlyPartyController(PartyService partyService) {
        this.partyService = partyService;
    }

    @GetMapping
    public ApiResponse<List<InstantlyPartiesResponse>> instantlyPartyList() {
        InstantlyParties instantlyParties = partyService.getInstantlyParties();
        List<InstantlyPartiesResponse> response = instantlyParties.stream()
                .map(party -> InstantlyPartiesResponse.of(party, instantlyParties.getAuthenticatedUser()))
                .toList();
        return ApiResults.success(response);
    }
}
