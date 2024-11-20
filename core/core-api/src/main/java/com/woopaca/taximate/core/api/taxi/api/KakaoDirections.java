package com.woopaca.taximate.core.api.taxi.api;

import com.woopaca.taximate.core.domain.party.model.Coordinate;
import org.springframework.util.CollectionUtils;

import java.util.List;

public record KakaoDirections(List<Route> routes) {

    public List<Coordinate> route() {
        List<Section> sections = routes.get(0)
                .sections;
        if (CollectionUtils.isEmpty(sections)) {
            return List.of();
        }

        return sections.get(0)
                .guides.stream()
                .map(guide -> new Coordinate(guide.x, guide.y))
                .toList();
    }

    public int fare() {
        Summary summary = routes.get(0)
                .summary;
        if (summary == null) {
            return 0;
        }
        return summary.fare
                .taxi;
    }

    public int duration() {
        Summary summary = routes.get(0)
                .summary;
        if (summary == null) {
            return 0;
        }
        return summary.duration;
    }

    record Route(Summary summary, List<Section> sections) {
    }

    record Summary(Fare fare, int duration) {
    }

    record Fare(int taxi) {
    }

    record Section(List<Guide> guides) {
    }

    record Guide(double x, double y) {
    }
}
