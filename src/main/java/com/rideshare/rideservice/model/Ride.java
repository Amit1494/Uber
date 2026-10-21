package com.rideshare.rideservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.ISBN;

import java.time.LocalDateTime;

@Entity
@Table(name="rides")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ride {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

//    Rider who requested the ride
    @Column(nullable=false)
    private String riderId;

//    WHO accepted the Ride(null until matched )
    @Column(nullable = false)
    private String driverId;

    @Column(nullable = false)
    private double pickupLatitude;

    @Column(nullable = false)
    private double pickupLongitude;

    @Column(nullable = false)
    private String pickupAddress;

    @Column(nullable = false)
    private double dropLatitude;
    @Column(nullable = false)
    private double dropLongitude;
    @Column(nullable = false)
    private String dropAddress;

//    Ride Status
    @Enumerated
    @Column(nullable = false)
    private RideStatus status;

//    Fare Details
    private double estimatedFare;
    private double actualFare;

//    TimeStamp
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private LocalDateTime startedAt;
    private LocalDateTime completedAt;

}
