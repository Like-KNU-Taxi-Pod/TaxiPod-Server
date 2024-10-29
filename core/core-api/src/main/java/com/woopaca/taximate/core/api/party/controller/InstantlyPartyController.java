package com.woopaca.taximate.core.api.party.controller;

import com.woopaca.taximate.core.api.common.model.ApiResults;
import com.woopaca.taximate.core.api.common.model.ApiResults.ApiResponse;
import com.woopaca.taximate.core.api.party.dto.request.CreateInstantlyPartyRequest;
import com.woopaca.taximate.core.api.party.dto.response.CreatePartyResponse;
import com.woopaca.taximate.core.api.party.dto.response.InstantlyPartiesResponse;
import com.woopaca.taximate.core.domain.party.InstantlyParties;
import com.woopaca.taximate.core.domain.party.Party;
import com.woopaca.taximate.core.domain.party.service.PartyService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
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

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ApiResponse<CreatePartyResponse> createParty(@Validated @RequestBody CreateInstantlyPartyRequest request) {
        Party newParty = request.toDomain();
        Long partyId = partyService.createInstantlyParty(newParty);
        CreatePartyResponse response = new CreatePartyResponse(partyId);
        return ApiResults.success(response);
    }
}
