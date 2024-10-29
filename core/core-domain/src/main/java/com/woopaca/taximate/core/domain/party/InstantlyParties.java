package com.woopaca.taximate.core.domain.party;

import com.woopaca.taximate.core.domain.user.User;
import lombok.Getter;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Getter
public class InstantlyParties {

    private List<Party> instantlyParties;
    private User authenticatedUser;

    public InstantlyParties(List<Party> parties, User authenticatedUser) {
        this.instantlyParties = Objects.requireNonNullElse(parties, Collections.emptyList());
        this.authenticatedUser = authenticatedUser;
    }

    public Stream<Party> stream() {
        return this.instantlyParties.stream();
    }
}
