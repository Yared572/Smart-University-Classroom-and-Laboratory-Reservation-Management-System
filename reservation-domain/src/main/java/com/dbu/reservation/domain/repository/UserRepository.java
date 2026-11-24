package com.dbu.reservation.domain.repository;
import com.dbu.reservation.domain.model.User;
import com.dbu.reservation.domain.model.Department;
import java.util.List;
import java.util.Optional;
public interface UserRepository { Optional<AuthenticatedUser> findForLogin(String universityId); void register(String name,String universityId,String email,String phone,long departmentId,com.dbu.reservation.domain.model.Role role,String passwordHash); List<Department> departments(); User getOrCreateGuest(); record AuthenticatedUser(User user,String passwordHash) { } }
