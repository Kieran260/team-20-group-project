/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.iamin.data.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

 @Table(name = "Tasks")

 @Entity
public class Tasks extends AbstractEntity
{
    
	
	private Integer supervisor_ID; 
        private Integer department_ID;
        private Integer Partecipent;
        private String description;
        private LocalDate dateTime;
        private LocalDate deadLine;
        private LocalDateTime dateModified;

	private LocalDate submissionDate;
        
       

	public Integer getsupervisor_ID() { 
	return supervisor_ID; 
	}

	public void setsupervisor_ID(Integer supervisor_ID) { 
	this.supervisor_ID = supervisor_ID; 
	}
        public Integer getdepartment_ID() { 
	return department_ID; 
	}

	public void setdepartment_ID(Integer department_ID) { 
	this.department_ID = department_ID; 
	}
         public Integer getPartecipent() { 
	return Partecipent; 
	}

	public void setPartecipent(Integer Partecipent) { 
	this.Partecipent = Partecipent; 
	}
        public String getdescription() { 
	return description; 
	}

	public void setdescription(String description) { 
	this.description = description; 
	}
        
        public LocalDate getSdateTime() {
            return dateTime;
        }  
        public void setdateTime(LocalDate dateTime ) {
        this.dateTime = dateTime;
        }
        public LocalDate getdeadLine() {
            return deadLine;
        }
        public void setdeadLine(LocalDate deadLine ) {
            this.deadLine = deadLine;
        }
        public LocalDateTime getDateModified() {
            return dateModified;
        }
       @PreUpdate 
	public void updateDateModified() {
        this.dateModified = LocalDateTime.now();
    }
   

     public LocalDate getsubmissionDate() {
        return submissionDate;
    }
    public void setsubmissionDate(LocalDate submissionDate) {
        this.submissionDate = submissionDate;
    }
}
