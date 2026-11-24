package com.dbu.reservation.application.service;
import com.dbu.reservation.domain.model.*;
import com.dbu.reservation.domain.repository.RoomRepository;
import java.time.*;
import java.util.List;
public final class AvailabilityService {private final RoomRepository rooms;public AvailabilityService(RoomRepository rooms){this.rooms=rooms;}public List<Room> search(RoomType type,LocalDate date,LocalTime start,LocalTime end,int people){if(date==null||date.isBefore(LocalDate.now()))throw new IllegalArgumentException("Choose a valid future date.");if(start==null||end==null||!start.isBefore(end))throw new IllegalArgumentException("Start time must be earlier than end time.");if(people<1)throw new IllegalArgumentException("Participant count must be positive.");return rooms.findAvailable(type,date,start,end,people);}}
