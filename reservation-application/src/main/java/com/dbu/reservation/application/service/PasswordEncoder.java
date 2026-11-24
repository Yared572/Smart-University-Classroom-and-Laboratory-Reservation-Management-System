package com.dbu.reservation.application.service;
public interface PasswordEncoder { String encode(String password); boolean matches(String password,String encoded); }
