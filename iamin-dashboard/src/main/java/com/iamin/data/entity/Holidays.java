/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.iamin.data.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.*;

 @Table(name = "Holidays")

public class Holidays extends AbstractEntity{


    
	
        private LocalDate startDate;
        private LocalDate endDate;
        private Integer totalDays;
        private LocalDateTime dateModified;
	private String authorisedBy;
        private LocalDate authorisedDate;
        private String deniedBy;
        private String deniedReason;
        
        
      
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
	public String getauthorisedBy() {
        return authorisedBy;
    }
    public void setauthorisedBy(String authorisedBy) {
        this.authorisedBy = authorisedBy;
    }
	
	public LocalDate authorisedDate() {
        return authorisedDate;
    }
    public void setauthorisedDate(LocalDate authorisedDate) {
        this.authorisedDate = authorisedDate;
    }
	public String getdeniedBy() {
        return deniedBy;
    }
    public void setdeniedBy(String deniedBy) {
        this.deniedBy = deniedBy;
    }
	
     public String getdeniedReason() {
        return deniedReason;
    }
    public void setdeniedReason(String deniedReason) {
        this.deniedReason = deniedReason;
    }
}

   

