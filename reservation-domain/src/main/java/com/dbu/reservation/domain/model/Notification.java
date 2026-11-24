package com.dbu.reservation.domain.model;
import java.time.LocalDateTime;
public record Notification(long id,long userId,String message,boolean read,LocalDateTime createdAt) { }
