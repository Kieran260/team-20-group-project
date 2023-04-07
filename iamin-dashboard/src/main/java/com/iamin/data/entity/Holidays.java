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
        private String holidayReason;
        private Boolean holidaysApproval;
        private String denyReason; 

     
        @ManyToOne
        @JoinColumn(name = "person_id")
        private SamplePerson person;


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
        public SamplePerson getPerson() {
            return person;
        }
        
        public void setPerson(SamplePerson person) {
            this.person = person;
        }
        public String getHolidayReason() {
            return holidayReason;
        }
        public void setHolidayReason(String holidayReason) {
            this.holidayReason = holidayReason;
        }
        public Boolean getHolidaysApproval() {
            return holidaysApproval;
        }
        public void setHolidaysApproval(Boolean holidaysApproval) {
            this.holidaysApproval = holidaysApproval;
        }
        public String getDenyReason() {
            return denyReason;
        }
        public void setDenyReason(String denyReason) {
            this.denyReason = denyReason;
        }
}
