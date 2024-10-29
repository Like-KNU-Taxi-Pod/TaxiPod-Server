CREATE TABLE IF NOT EXISTS `party`
(
    `id`                   BIGINT          NOT NULL AUTO_INCREMENT,
    `title`                VARCHAR(30)     NOT NULL,           # 팟 제목
    `explanation`          VARCHAR(500),                       # 간단 설명
    `departure_time`       DATETIME        NOT NULL,           # 출발 시간
    `origin`               VARCHAR(40)     NOT NULL,           # 출발지 이름
    `destination`          VARCHAR(40)     NOT NULL,           # 도착지 이름
    `origin_address`       VARCHAR(80)     NOT NULL,           # 출발지 주소
    `destination_address`  VARCHAR(80)     NOT NULL,           # 도착지 주소
    `origin_location`      POINT SRID 4326 NOT NULL,           # 출발지 좌표
    `destination_location` POINT SRID 4326 NOT NULL,           # 도착지 좌표
    `max_participants`     INT             NOT NULL,           # 최대 탑승 인원
    `views`                INT             NOT NULL DEFAULT 0, # 조회수
    `created_at`           DATETIME        NOT NULL,           # 생성 시간
    `updated_at`           DATETIME        NOT NULL,           # 수정 시간
    PRIMARY KEY (`id`)
);

CREATE SPATIAL INDEX `sidx_origin_location` ON `party` (`origin_location`);

CREATE SPATIAL INDEX `sidx_destination_location` ON `party` (`destination_location`);

CREATE TABLE IF NOT EXISTS `user`
(
    `id`                      BIGINT      NOT NULL AUTO_INCREMENT,
    `email`                   VARCHAR(40) NOT NULL,
    `nickname`                VARCHAR(10) NOT NULL,
    `profile_image`           VARCHAR(1000),
    `provider`                CHAR(6)     NOT NULL DEFAULT 'KAKAO',
    `status`                  CHAR(8)     NOT NULL DEFAULT 'ACTIVE',
    `push_notification_token` VARCHAR(45),
    `created_at`              DATETIME    NOT NULL,
    `updated_at`              DATETIME    NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE UNIQUE INDEX `uidx_email` ON `user` (`email`);

CREATE TABLE IF NOT EXISTS `participation`
(
    `id`         BIGINT   NOT NULL AUTO_INCREMENT,
    `role`       CHAR(11) NOT NULL,
    `status`     CHAR(13) NOT NULL,
    `user_id`    BIGINT   NOT NULL,
    `party_id`   BIGINT   NOT NULL,
    `created_at` DATETIME NOT NULL,
    `updated_at` DATETIME NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE INDEX `idx_party_id` ON `participation` (`party_id`);

CREATE INDEX `idx_user_id` ON `participation` (`user_id`);

CREATE TABLE IF NOT EXISTS `chat`
(
    `id`         BIGINT       NOT NULL AUTO_INCREMENT,
    `message`    VARCHAR(500) NOT NULL,
    `type`       VARCHAR(20)  NOT NULL,
    `user_id`    BIGINT,
    `party_id`   BIGINT       NOT NULL,
    `created_at` DATETIME     NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE INDEX `idx_party_id` ON `chat` (`party_id`);

CREATE TABLE IF NOT EXISTS `chat_read`
(
    `id`           BIGINT NOT NULL AUTO_INCREMENT,
    `user_id`      BIGINT NOT NULL,
    `party_id`     BIGINT NOT NULL,
    `last_chat_id` BIGINT NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE UNIQUE INDEX `uidx_user_id_party_id` ON `chat_read` (`user_id`, `party_id`);

CREATE TABLE IF NOT EXISTS `push_token`
(
    `id`         BIGINT      NOT NULL AUTO_INCREMENT,
    `user_id`    BIGINT      NOT NULL,
    `token`      VARCHAR(63) NOT NULL,
    `created_at` DATETIME    NOT NULL,
    `updated_at` DATETIME    NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE INDEX `idx_user_id` ON `push_token` (`user_id`);

CREATE UNIQUE INDEX `uidx_token` ON `push_token` (`token`);

CREATE TABLE IF NOT EXISTS `instantly_party`
(
    `id`               BIGINT      NOT NULL AUTO_INCREMENT,
    `title`            VARCHAR(30) NOT NULL, # 팟 제목
    `max_participants` INT         NOT NULL, # 최대 탑승 인원
    `created_at`       DATETIME    NOT NULL, # 생성 시간
    `updated_at`       DATETIME    NOT NULL, # 수정 시간
    PRIMARY KEY (`id`)
);

CREATE INDEX `idx_created_at` ON `instantly_party` (`created_at`);
