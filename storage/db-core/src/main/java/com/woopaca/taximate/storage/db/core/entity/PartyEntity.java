package com.woopaca.taximate.storage.db.core.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import lombok.Builder;
import lombok.Getter;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;

@Getter
@Entity(name = "party")
public class PartyEntity extends BaseEntity {

    private String title;

    private String explanation;

    private LocalDateTime departureTime;

    private String origin;

    private String destination;

    private String originAddress;

    private String destinationAddress;

    private Point originLocation;

    private Point destinationLocation;

    private int maxParticipants;

    private int views;

    @Transient
//    @OneToMany(mappedBy = "party")
    private Set<ParticipationEntity> participationSet = Collections.emptySet();

    public PartyEntity() {
    }

    @Builder
    public PartyEntity(String title, String explanation, LocalDateTime departureTime, String origin, String destination,
                       String originAddress, String destinationAddress, double originLatitude, double originLongitude,
                       double destinationLatitude, double destinationLongitude, int maxParticipants) {
        this.title = title;
        this.explanation = explanation;
        this.departureTime = departureTime;
        this.origin = origin;
        this.destination = destination;
        this.originAddress = originAddress;
        this.destinationAddress = destinationAddress;
        this.maxParticipants = maxParticipants;

        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        this.originLocation = geometryFactory.createPoint(new Coordinate(originLongitude, originLatitude));
        this.destinationLocation = geometryFactory.createPoint(new Coordinate(destinationLongitude, destinationLatitude));
    }

    public double getOriginLatitude() {
        return originLocation.getY();
    }

    public double getOriginLongitude() {
        return originLocation.getX();
    }

    public double getDestinationLatitude() {
        return destinationLocation.getY();
    }

    public double getDestinationLongitude() {
        return destinationLocation.getX();
    }
}
