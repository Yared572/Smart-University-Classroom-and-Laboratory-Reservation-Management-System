package com.dbu.reservation.domain.model;
public record Room(long id,String name,RoomType type,String building,int floor,int capacity,String equipment,String status) { @Override public String toString(){return name+" ("+type+", "+building+", capacity "+capacity+")";} }
