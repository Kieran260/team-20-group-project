/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.iamin.data.entity;
import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.*;

@Entity
// Adding the table name
@Table(name = "staffPayRate")

public class StaffPayRate extends AbstractEntity implements Serializable {



	@OneToOne
    @JoinColumn(name = "user_id")
    private User user; // Foreign Key for user table
    private Integer currency;
    private String staffRate; // FullTime or PartTime
    private String payType; // If Salary or Hourly
    private Float payRate; // Amount
    private LocalDateTime dateModified;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

	public Integer currency() { 
	return currency; 
	}

	public void setcurrency(Integer currency) { 
	this.currency = currency; 
	}
	public String staffRate() { 
	return staffRate; 
	}

	public void setstaffRate(String staffRate) { 
	this.staffRate = staffRate; 
	}
	public String payType() { 
	return payType; 
	}

	public void setpayType(String payType) { 
	this.payType = payType; 
	}
	public Float payRate() { 
	return payRate; 
	}

	public void setpayRate(Float payRate) { 
	this.payRate = payRate; 
	}
	 public LocalDateTime getDateModified() {
         return dateModified;
     }
    @PreUpdate 
	public void updateDateModified() {
     this.dateModified = LocalDateTime.now();
 }

		
}
