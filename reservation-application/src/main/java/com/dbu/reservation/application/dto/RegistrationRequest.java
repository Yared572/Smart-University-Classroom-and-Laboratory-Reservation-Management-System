package com.dbu.reservation.application.dto;
import com.dbu.reservation.domain.model.Role;
public record RegistrationRequest(String fullName,String universityId,String email,String phone,long departmentId,String password,Role role) { }
