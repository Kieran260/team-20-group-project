/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.iamin.data.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

 @Entity
@Table(name = "Events")
public class Events extends AbstractEntity implements Serializable {
    @ManyToOne
    @JoinColumn(name = "organiser_id")
    private User organiserId;
    
    @OneToMany
    private List<User> attendees;
        
    private String title;
    private String description;
    private String location;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer totalDays;
    private LocalDateTime dateModified;

    public User getOrganiserId() { 
        return organiserId; 
    }

    public void setOrganiserId(User organiserId) { 
        this.organiserId = organiserId; 
    }
    
    public List<User> getAttendees() { 
        return attendees; 
    }

    public void setAttendees(List<User> attendees) { 
        this.attendees = attendees; 
    }
    
         public String gettitle() { 
	return title; 
	}

	public void settitle(String title) { 
	this.title = title; 
	}
        public String getdescription() { 
	return description; 
	}

	public void setdescription(String description) { 
	this.description = description; 
	}
        public String getDlocation() { 
	return location; 
	}

	public void setlocation(String location) { 
	this.location = location; 
	}
        public LocalDate getstartDate() {
            return startDate;
        }  
        public void setstartDate(LocalDate startDate ) {
        this.startDate = startDate;
        }
        public LocalDate getendDate() {
            return endDate;
        }
        public void setendDate(LocalDate endDate ) {
            this.endDate = endDate;
        }
        public Integer gettotalDays() {
            return totalDays;
        }
        public LocalDateTime getDateModified() {
            return dateModified;
        }
       @PreUpdate 
	public void updateDateModified() {
        this.dateModified = LocalDateTime.now();
    }
  
  
    
}
