package com.rideshare.rideservice.event;

//    Event published to kafka when a ride is requested
//    Matching service consumes this event
//    Topid ride.requested
public class RideRequestedEvent {
    private String rideId;
    private String riderId;
    private
}
