package com.dbu.reservation.domain.model;
import java.time.*;
public record Reservation(long id,String requester,String room,LocalDate date,LocalTime start,LocalTime end,int participants,String purpose,ReservationStatus status,String rejectionReason) { }
