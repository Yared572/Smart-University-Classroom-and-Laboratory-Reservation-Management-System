package com.dbu.reservation.application.service;
import com.dbu.reservation.application.dto.RegistrationRequest;
import com.dbu.reservation.domain.model.User;
import com.dbu.reservation.domain.repository.UserRepository;
public final class AuthenticationService {
 private final UserRepository users; private final PasswordEncoder encoder;
 public AuthenticationService(UserRepository users,PasswordEncoder encoder){this.users=users;this.encoder=encoder;}
 public User login(String universityId,String password){var account=users.findForLogin(universityId.trim()).orElseThrow(()->new IllegalArgumentException("Invalid university ID or password."));if(!encoder.matches(password,account.passwordHash()))throw new IllegalArgumentException("Invalid university ID or password.");if(!"ACTIVE".equals(account.user().status()))throw new IllegalArgumentException("Account is pending activation or disabled.");return account.user();}
 public void register(RegistrationRequest request){if(request.fullName().isBlank()||request.universityId().isBlank()||request.email().isBlank()||request.password().length()<8||request.role()==null||request.role()==com.dbu.reservation.domain.model.Role.ADMIN)throw new IllegalArgumentException("Complete required fields. Choose Student or Lecturer; password must be at least 8 characters.");users.register(request.fullName(),request.universityId(),request.email(),request.phone(),request.departmentId(),request.role(),encoder.encode(request.password()));}
}
