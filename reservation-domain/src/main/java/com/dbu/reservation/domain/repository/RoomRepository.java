package com.dbu.reservation.domain.repository;
import com.dbu.reservation.domain.model.*;
import java.time.*;
import java.util.List;
public interface RoomRepository { List<Room> findAvailable(RoomType type,LocalDate date,LocalTime start,LocalTime end,int participants); }
