package com.iamin.data.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

@Entity
@Table(name = "absence")
public class Absence extends AbstractEntity {

	private String absenceReason;
	private String authorisedBy; 
    @Column(columnDefinition = "TEXT")
	private String documentsURL;
    private LocalDateTime dateModified;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean absenceApproval;
    @ManyToOne
    @JoinColumn(name = "person_id")
    private SamplePerson person;

	public SamplePerson getPerson() {
        return person;
    }

    public void setPerson(SamplePerson person) {
        this.person = person;
    }

    public String absenceReason() { 
	return absenceReason; 
	}

	public void setAbsenceReason(String absenceReason) { 
	this.absenceReason = absenceReason; 
	}
	public String authorisedBy() { 
	return authorisedBy; 
	}

	public void setAuthorisedBy(String authorisedBy) { 
	this.authorisedBy = authorisedBy; 
	}
	public String documentsURL() { 
	return documentsURL; 
	}

	public void setDocumentsURL(String documentsURL) { 
	this.documentsURL = documentsURL; 
	}
    
	public LocalDateTime getDateModified() {
         return dateModified;
    }
    @PreUpdate 
	public void updateDateModified() {
     this.dateModified = LocalDateTime.now();
    }
    	public LocalDate getStartDate() {
        return startDate;
    }
    public void setStartDate(LocalDate startDate ) {
        this.startDate = startDate;
    }
    public LocalDate getSendDate() {
        return endDate;
    }
    public void setEndDate(LocalDate endDate ) {
        this.endDate = endDate;
    }
    public Boolean getAbsenceApproval() {
        return absenceApproval;
    }
    public void setAbsenceApproval(Boolean absenceApproval ) {
        this.absenceApproval = absenceApproval;
    }
}