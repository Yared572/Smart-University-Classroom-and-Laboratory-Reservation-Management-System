package com.dbu.reservation.application.dto;
import com.dbu.reservation.domain.model.Room;
import java.time.*;
public record ReservationRequest(Room room,LocalDate date,LocalTime start,LocalTime end,int participants,String purpose,String description) { }
