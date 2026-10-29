package com.rideshare.rideservice.service;
import java.util.*;
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
import org.springframework.web.servlet.HandlerMapping;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class RideService {
    private final RideRepository rideRepository;
    private final KafkaTemplate<String, RideRequestedEvent> kafkaTemplate;
    private static final String RIDE_REQUESTED_TOPIC="ride.requested";
    private final HandlerMapping resourceHandlerMapping;

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
                savedRide.getId(),
                savedRide.getRiderId(),
                savedRide.getPickupLatitude(),
                savedRide.getPickupLongitude(),
                savedRide.getPickupAddress(),
                savedRide.getDropLatitude(),
                savedRide.getDropLongitude(),
                savedRide.getDropAddress()
        );
        kafkaTemplate.send(RIDE_REQUESTED_TOPIC,savedRide.getId(),event);
        log.info("Ride Requested Event published to kafka for ride:{}",savedRide.getId());

//        Update Status to Matching
        savedRide.setStatus(RideStatus.MATCHING);
        rideRepository.save(savedRide);
        return mapToResponse(savedRide);

//        Step 2: Publish event to Kafka
//        Matching Service will consume this and find nearest driver
    }
    public void updateRideWithDriver(String rideId,String driverId){
        Ride ride=rideRepository.findById(rideId).orElseThrow(()->new RuntimeException("Ride Not Found"));
        ride.setDriverId(driverId);

        ride.setStatus(RideStatus.ACCEPTED);
        rideRepository.save(ride);
    }

    private double calculateEstimateFare(RideRequest request) {
//            SImplified haversine distance calculation
        double lat1=Math.toRadians(request.getPickupLatitude());
        double lat2=Math.toRadians(request.getDropLatitude());

        double long1=Math.toRadians(request.getPickupLongitude());
        double long2=Math.toRadians(request.getDropLongitude());

        double latDiff=lat2-lat1;
        double longdiff=long2-long1;

        double a=Math.pow(Math.sin(latDiff/2),2)+Math.cos(lat1)*Math.cos(lat2)*Math.pow(Math.sin(longdiff/2),2);
        double c=2*Math.asin(Math.sqrt(a));
        double distance=6371*c;

        double basefare=50+(12*distance);
        return Math.round(basefare*100.0)/100.0;



    }

    private RideResponse mapToResponse(Ride ride) {
        RideResponse rideResponse=new RideResponse();
        rideResponse.setId(ride.getId());
        rideResponse.setRiderId(ride.getRiderId());
        rideResponse.setDriverId(ride.getDriverId());
        rideResponse.setPickupAddress(ride.getPickupAddress());
        rideResponse.setPickupLatitude(ride.getPickupLatitude());
        rideResponse.setPickupLongitude(ride.getPickupLongitude());
        rideResponse.setDropAddress(ride.getDropAddress());
        rideResponse.setDropLatitude(ride.getDropLatitude());
        rideResponse.setDropLongitude(ride.getDropLongitude());
        rideResponse.setStatus(ride.getStatus());
        rideResponse.setEstimatedFare(ride.getEstimatedFare());
        rideResponse.setActualFare(ride.getActualFare());
        rideResponse.setCreatedAt(ride.getCreatedAt());
        rideResponse.setStartedAt(ride.getStartedAt());
        rideResponse.setCompletedAt(ride.getCompletedAt());
        return rideResponse;
    }

    public RideResponse getRideById(String rideId) {
        Ride ride=rideRepository.findById(rideId).orElseThrow(()->new RuntimeException("Ride Not found "));
        return mapToResponse(ride);


    }

    public RideResponse cancelRide(String rideId) {

        Ride ride=rideRepository.findById(rideId).orElseThrow(()->new RuntimeException("Ride Not found"));
        if(ride.getStatus()!=RideStatus.RIDE_STARTED){
            throw new RuntimeException("Ride cannot be cancelled current status is "+ride.getStatus());
        }
        ride.setStatus(RideStatus.CANCELLED);
        rideRepository.save(ride);
        return mapToResponse(ride);

    }

    public RideResponse endRide(String rideId) {

        Ride ride=rideRepository.findById(rideId).orElseThrow(()->new RuntimeException("Ride Not found"));
        if(ride.getStatus()!=RideStatus.RIDE_STARTED){
            throw new RuntimeException("Ride cannot be completed. Current Status "+ride.getStatus());
        }
        ride.setStatus(RideStatus.COMPLETED);
        ride.setCompletedAt(LocalDateTime.now());
        ride.setActualFare(ride.getEstimatedFare());

        rideRepository.save(ride);
        return mapToResponse(ride);

    }

    public RideResponse startRide(String rideId) {
        Ride ride=rideRepository.findById(rideId).orElseThrow(()->new RuntimeException("Ride Not Found"));
        if(ride.getStatus()!=RideStatus.ACCEPTED){
            throw new RuntimeException("Ride Cannot be started.Current Status is "+ride.getStatus());

        }
        ride.setStatus(RideStatus.RIDE_STARTED);
        ride.setStartedAt(LocalDateTime.now());
        rideRepository.save(ride);
        return mapToResponse(ride);

    }

    public List<RideResponse> getRidesByRider(String riderId) {
        return rideRepository.findByRiderIdOrderByCreatedAtDesc(riderId).stream().map(this::mapToResponse).collect(Collectors.toList());
    }
}
