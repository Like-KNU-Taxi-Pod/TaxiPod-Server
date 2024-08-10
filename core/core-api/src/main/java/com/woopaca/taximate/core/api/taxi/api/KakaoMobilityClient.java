package com.woopaca.taximate.core.api.taxi.api;

import com.woopaca.taximate.core.api.party.model.Coordinate;
import com.woopaca.taximate.core.api.taxi.domain.Taxi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;

@Slf4j
@Component
public class KakaoMobilityClient {

    private final RestClient restClient;
    private final KakaoMobilityProperties kakaoMobilityProperties;

    public KakaoMobilityClient(RestClient restClient, KakaoMobilityProperties kakaoMobilityProperties) {
        this.restClient = restClient;
        this.kakaoMobilityProperties = kakaoMobilityProperties;
    }

    /**
     * 택시 이동 경로 및 요금•시간 조회
     * @param origin 출발지
     * @param destination 도착지
     * @return 택시 이동 경로 및 요금•시간
     * @see <a href="https://developers.kakaomobility.com/docs/navi-api/directions/">카카오모빌리티 길찾기 REST API</a>
     */
    public Taxi requestTaxi(Coordinate origin, Coordinate destination) {
        try {
            KakaoDirections directions = restClient.get()
                    .uri(UriComponentsBuilder.fromUriString(kakaoMobilityProperties.getDirectionsUrl())
                            .queryParam("origin", String.format("%f,%f", origin.longitude(), origin.latitude()))
                            .queryParam("destination", String.format("%f,%f", destination.longitude(), destination.latitude()))
                            .build()
                            .toUri())
                    .header(HttpHeaders.AUTHORIZATION, "KakaoAK " + kakaoMobilityProperties.getApiKey())
                    .retrieve()
                    .body(KakaoDirections.class);
            if (directions == null) {
                return new Taxi(Collections.emptyList(), 0, 0);
            }

            return new Taxi(directions.route(), directions.fare(), directions.duration());
        } catch (HttpStatusCodeException exception) {
            log.warn("카카오 모빌리티 길찾기 요청 오류", exception);
            //TODO Handle kakao mobility request failure
            throw exception;
        }
    }
}