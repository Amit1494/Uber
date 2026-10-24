package com.rideshare.rideservice.model;

//Flow :Requested ->Matching ->Accepted->Driver_Arriving->
//                             RideStarted->Completed
//                             Cancelled(Can Happen at multiple stages)


public enum RideStatus {

    REQUESTED,
    MATCHING,
    ACCEPTED,ARRIVING,RIDE_STARTED,CANCELLED,
    COMPLETED
}
