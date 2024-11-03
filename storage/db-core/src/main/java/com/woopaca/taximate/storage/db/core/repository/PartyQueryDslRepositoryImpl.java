package com.woopaca.taximate.storage.db.core.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.woopaca.taximate.storage.db.core.entity.PartyEntity;
import jakarta.persistence.LockModeType;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.woopaca.taximate.storage.db.core.entity.QParticipationEntity.participationEntity;
import static com.woopaca.taximate.storage.db.core.entity.QPartyEntity.partyEntity;

@Repository
public class PartyQueryDslRepositoryImpl implements PartyQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private final GeometryFactory geometryFactory;

    public PartyQueryDslRepositoryImpl(JPAQueryFactory queryFactory, GeometryFactory geometryFactory) {
        this.queryFactory = queryFactory;
        this.geometryFactory = geometryFactory;
    }

    @Override
    public List<PartyEntity> findContainsDepartureAfter(double minLatitude, double minLongitude,
                                                        double maxLatitude, double maxLongitude,
                                                        LocalDateTime after) {
        Polygon polygon = geometryFactory.createPolygon(new Coordinate[]{
                new Coordinate(minLongitude, minLatitude),
                new Coordinate(maxLongitude, minLatitude),
                new Coordinate(maxLongitude, maxLatitude),
                new Coordinate(minLongitude, maxLatitude),
                new Coordinate(minLongitude, minLatitude)
        });
        return queryFactory.select(partyEntity)
                .from(partyEntity)
                .leftJoin(partyEntity.participationSet, participationEntity).fetchJoin()
                .where(partyEntity.originLocation.within(polygon).and(partyEntity.departureTime.after(after)))
                .orderBy(partyEntity.departureTime.asc())
                .fetch();
    }

    @Override
    public Optional<PartyEntity> findByIdForUpdate(Long partyId) {
        return Optional.ofNullable(queryFactory.selectFrom(partyEntity)
                .where(partyEntity.id.eq(partyId))
                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                .fetchOne());
    }
}
