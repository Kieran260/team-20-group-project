package com.iamin.data.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

@Entity
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
        private String reason;

     
        @ManyToOne
        @JoinColumn(name = "person_id")
        private SamplePerson person;

        public String getReason() {
            return reason;
        }
        public void setReason(String reason) {
            this.reason = reason;
        }

        public LocalDate getStartDate() {
            return startDate;
        }  
        public void setStartDate(LocalDate startDate ) {
        this.startDate = startDate;
        }
        public LocalDate getEndDate() {
            return endDate;
        }
        public void setEndDate(LocalDate endDate ) {
            this.endDate = endDate;
        }
        public Integer getTotalDays() {
            return totalDays;
        }
        public void setTotalDays(Integer totalDays) {
            this.totalDays = totalDays;
        }
        public LocalDateTime getDateModified() {
            return dateModified;
        }
        @PreUpdate 
        public void updateDateModified() {
            this.dateModified = LocalDateTime.now();
        }
        public String getAuthorisedBy() {
            return authorisedBy;
        }
        public void setAuthorisedBy(String authorisedBy) {
            this.authorisedBy = authorisedBy;
        }
        
        public LocalDate getAuthorisedDate() {
            return authorisedDate;
        }
        public void setAuthorisedDate(LocalDate authorisedDate) {
            this.authorisedDate = authorisedDate;
        }
        public String getDeniedBy() {
            return deniedBy;
        }
        public void setDeniedBy(String deniedBy) {
            this.deniedBy = deniedBy;
        }
        
        public String getDeniedReason() {
            return deniedReason;
        }
        public void setDeniedReason(String deniedReason) {
            this.deniedReason = deniedReason;
        }
        public SamplePerson getPerson() {
            return person;
        }
        
        public void setPerson(SamplePerson person) {
            this.person = person;
        }
    
}
