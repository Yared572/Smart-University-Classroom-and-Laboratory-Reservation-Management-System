package com.dbu.reservation.domain.model;
import java.time.LocalDateTime;
public record Approval(long id,long reservationId,long adminId,ReservationStatus status,String reason,LocalDateTime processedAt) { }
