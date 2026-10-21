package com.rideshare.rideservice.service;

import com.rideshare.rideservice.dto.RideRequest;
import com.rideshare.rideservice.dto.RideResponse;
import com.rideshare.rideservice.event.RideRequestedEvent;
import com.rideshare.rideservice.model.Ride;
import com.rideshare.rideservice.model.RideStatus;
import com.rideshare.rideservice.repository.RideRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RideService {
    private final RideRepository rideRepository;
    private final KafkaTemplate<String, RideRequestedEvent> kafkaTemplate;
    private static final String RIDE_REQUESTED_TOPIC="ride.requested";

    public RideResponse requestRide(RideRequest request) {
        log.info("New Ride request from rider: {}",request.getRiderId());
//        Step 1 Save ride to db
        Ride ride=new Ride();
        ride.setRiderId(request.getRiderId());
        ride.setPickupAddress(request.getPickupAddress());
        ride.setPickupLatitude(request.getPickupLatitude());
        ride.setPickupLongitude(request.getPickupLongitude());
        ride.setDropAddress(request.getDropAddress());
        ride.setDropLatitude(request.getDropLatitude());
        ride.setDropLongitude(request.getDropLongitude());
        ride.setStatus(RideStatus.REQUESTED);
        ride.setEstimatedFare(calculateEstimateFare(request));

        Ride savedRide=rideRepository.save(ride);


        RideRequestedEvent event=new RideRequestedEvent(

        )

//        Step 2: Publish event to Kafka
//        Matching Service will consume this and find nearest driver
    }

    public RideResponse getRideById(String rideId) {

    }

    public RideResponse cancelRide(String rideId) {

    }

    public RideResponse endRide(String rideId) {

    }

    public RideResponse startRide(String rideId) {

    }

    public List<RideResponse> getRidesByRider(String riderId) {

    }
}
