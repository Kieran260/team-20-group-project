/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.iamin.data.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.persistence.OneToOne;
import javax.persistence.PreUpdate;



public class ClockInOut extends AbstractEntity {
	@OneToOne(mappedBy = "user")
    private Integer user_ID; //Foreign Key to user Table
    private LocalTime clockinTime;
    private LocalTime clockOutTime;
    private LocalDate date;
    private LocalDateTime dateModified;
    private Boolean unathorised;
    
    public Integer user_ID() {
        return user_ID;
    }
    public void setdateModified(Integer user_ID) {
        this.user_ID = user_ID;
    }
    public LocalDateTime getDateModified() {
        return dateModified;
    }
   @PreUpdate 
public void updateDateModified() {
    this.dateModified = LocalDateTime.now();
}
    public LocalTime getclockinTime() {
        return clockinTime;
    }
    public void setclockinTime(LocalTime clockinTime) {
        this.clockinTime = clockinTime;
    }
    public LocalTime getclockOutTime() {
        return clockOutTime;
    }
    public void setclockOutTime(LocalTime clockOutTime) {
        this.clockOutTime = clockOutTime;
    }
    public LocalDate getdate() {
        return date;
    }
    public void setdate(LocalDate date) {
        this.date = date;
    }
    public boolean getunathorised() {
        return unathorised;
    }
    public void setunathorised(boolean unathorised) {
        this.unathorised = unathorised;
    }
}
