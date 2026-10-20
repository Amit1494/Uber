package com.rideshare.rideservice.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class RideRequest {

    @NotBlank(message="Rider Id is required")
    private String riderId;

    @NotNull(message="Pickup Location Details is required(Latitude)")
    private double pickupLatitude;
    @NotNull(message="Pickup Location Details is required(Longitude)")
    private double pickupLongitude;
    @NotNull(message="Pickup Location Details is required(Address)")
    private String pickupAddress;

    @NotNull(message="Drop Location Details is required(Latitude)")
    private double dropLatitude;
    @NotNull(message="Drop Location Details is required(Longitude)")
    private double dropLongitude;
    @NotNull(message="Drop Location Details is required(Address)")
    private String dropAddress;

}
