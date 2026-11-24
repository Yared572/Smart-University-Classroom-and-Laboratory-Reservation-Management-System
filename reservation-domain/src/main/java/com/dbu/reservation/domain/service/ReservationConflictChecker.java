package com.dbu.reservation.domain.service;
import java.time.LocalTime;
public final class ReservationConflictChecker { private ReservationConflictChecker(){} public static boolean overlaps(LocalTime aStart,LocalTime aEnd,LocalTime bStart,LocalTime bEnd){return aStart.isBefore(bEnd)&&aEnd.isAfter(bStart);} }
