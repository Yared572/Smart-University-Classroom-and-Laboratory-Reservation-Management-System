package com.dbu.reservation.domain.repository;
import com.dbu.reservation.domain.model.*;
import java.time.*;
import java.util.List;
public interface ReservationRepository { void request(long userId,long roomId,LocalDate date,LocalTime start,LocalTime end,int participants,String purpose,String description); List<Reservation> findByUser(long userId); List<Reservation> findPending(); void decide(long reservationId,long adminId,boolean approve,String rejectionReason); }
