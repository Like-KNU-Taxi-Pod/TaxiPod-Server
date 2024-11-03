package com.woopaca.taximate.core.domain.error;

import lombok.Getter;

@Getter
public enum ErrorType {

    // TODO error code 상세 설정 및 오류 형식별 type 분리하기
    COORDINATE_OUT_OF_RANGE("", ErrorHttpStatus.BAD_REQUEST),
    INVALID_MAP_RANGE("", ErrorHttpStatus.BAD_REQUEST),

    NONEXISTENT_PARTY("", ErrorHttpStatus.NOT_FOUND),
    NONEXISTENT_USER("", ErrorHttpStatus.NOT_FOUND),

    PARTICIPANTS_COUNT("", ErrorHttpStatus.BAD_REQUEST),
    PARTICIPATION_LIMIT("", ErrorHttpStatus.BAD_REQUEST),

    TITLE_TOO_LONG("", ErrorHttpStatus.BAD_REQUEST),
    EXPLANATION_TOO_LONG("", ErrorHttpStatus.BAD_REQUEST),
    NONEXISTENT_ADDRESS("", ErrorHttpStatus.BAD_REQUEST),
    PAST_DEPARTURE_TIME("", ErrorHttpStatus.BAD_REQUEST),

    PARTY_ALREADY_ENDED("", ErrorHttpStatus.BAD_REQUEST),
    PARTY_ALREADY_PARTICIPATED("", ErrorHttpStatus.BAD_REQUEST),
    PARTICIPANTS_FULL("", ErrorHttpStatus.BAD_REQUEST),
    NOT_PARTICIPATED_PARTY("", ErrorHttpStatus.BAD_REQUEST),

    MESSAGE_TOO_LONG("", ErrorHttpStatus.BAD_REQUEST),

    INVALID_REFRESH_TOKEN("", ErrorHttpStatus.UNAUTHORIZED);

    private final String errorCode;
    private final ErrorHttpStatus errorHttpStatus;

    ErrorType(String errorCode, ErrorHttpStatus errorHttpStatus) {
        this.errorCode = errorCode;
        this.errorHttpStatus = errorHttpStatus;
    }

    @Getter
    public enum ErrorHttpStatus {

        BAD_REQUEST(400, "Bad Request"),
        UNAUTHORIZED(401, "Unauthorized"),
        NOT_FOUND(404, "Not Found");

        private final int value;
        private final String reasonPhrase;

        ErrorHttpStatus(int value, String reasonPhrase) {
            this.value = value;
            this.reasonPhrase = reasonPhrase;
        }
    }
}
