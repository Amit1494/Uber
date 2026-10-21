package com.rideshare.rideservice.controller;

import com.rideshare.rideservice.dto.RideRequest;
import com.rideshare.rideservice.dto.RideResponse;
import com.rideshare.rideservice.service.RideService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/rides")
@Slf4j
@RequiredArgsConstructor
public class RideController {

    private final RideService rideService;
    @PostMapping("/request")
    public ResponseEntity<RideResponse> requestRide(
            @Valid @RequestBody RideRequest rideRequest){
        log.info("Ride Request Received from rider:{}",rideRequest.getRiderId());
        return ResponseEntity.status(HttpStatus.CREATED).body(rideService.requestRide());

    }
    @GetMapping("/{rideId}")
    public ResponseEntity<RideResponse> getRideById(
            @PathVariable String rideId
    ){
        return ResponseEntity.ok(rideService.getRideById(rideId));
    }
    @GetMapping("rider/{riderId}")
    public ResponseEntity<List<RideResponse>> getRideByRider(
            @PathVariable String riderId
    ){
        return ResponseEntity.ok(rideService.getRidesByRider(riderId));
    }
    @PutMapping("/{rideId}/start")
    public ResponseEntity<RideResponse> startRide(
            @PathVariable String rideId
    ){
        return ResponseEntity.ok(rideService.startRide(rideId));
    }

    @PutMapping("/{rideId}/end")
    public ResponseEntity<RideResponse> endRide(
            @PathVariable String rideId
    ){
        return ResponseEntity.ok(rideService.endRide(rideId));
    }
    @PutMapping("/{rideId}/cancel")
    public ResponseEntity<RideResponse> cancelRide(
            @PathVariable String rideId
    ){
        return ResponseEntity.ok(rideService.cancelRide(rideId));
    }


}
