package com.dbu.reservation.application.service;
import com.dbu.reservation.domain.model.*;
import com.dbu.reservation.domain.repository.ReservationRepository;
import java.util.List;
public final class ApprovalService {private final ReservationRepository reservations;public ApprovalService(ReservationRepository reservations){this.reservations=reservations;}public List<Reservation> pending(User admin){requireAdmin(admin);return reservations.findPending();}public void decide(User admin,long id,boolean approve,String reason){requireAdmin(admin);if(!approve&&(reason==null||reason.isBlank()))throw new IllegalArgumentException("Enter a reason for rejection.");reservations.decide(id,admin.id(),approve,reason);}private void requireAdmin(User user){if(user==null||user.role()!=Role.ADMIN)throw new IllegalArgumentException("Administrator access required.");}}
