package com.iamin.data.entity;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PreUpdate;
import javax.persistence.Table;


@Table(name = "Tasks")
@Entity
public class Tasks extends AbstractEntity
{
    

    @ManyToOne
	@JoinColumn(name = "person_id")
	private SamplePerson person;  
    private LocalDate submittedDate;  
    private String title;
    private String description;
    private LocalDate deadLine;
    private LocalDateTime dateModified;
    private LocalDate assignDate;
    private LocalDate ackDate;
    private boolean completed;
    
    public SamplePerson getPerson() {
        return person;
    }
    public void setPerson(SamplePerson person) {
        this.person = person;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    
    public LocalDate getDeadLine() {
        return deadLine;
    }
    public void setDeadLine(LocalDate deadLine) {
        this.deadLine = deadLine;
    }
    
    public LocalDateTime getDateModified() {
		return dateModified;
    }

    @PreUpdate
    public void updateDateModified() {
    	this.dateModified = LocalDateTime.now();
    }
    
    public LocalDate getAssignDate() {
        return assignDate;
    }
    public void setAssignDate(LocalDate assignDate) {
        this.assignDate = assignDate;
    }
    
    public boolean isCompleted() {
        return completed;
    }
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
    
    public LocalDate getSubmittedDate() {
        return submittedDate;
    }
    public void setSubmittedDate(LocalDate submittedDate) {
        this.submittedDate = submittedDate;
    }

    public LocalDate getAckDate() {
        return submittedDate;
    }
    public void setAckDate() {
        this.ackDate = LocalDate.now();
    }
}