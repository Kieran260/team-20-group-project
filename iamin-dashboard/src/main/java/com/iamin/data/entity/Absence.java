package com.iamin.data.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.OneToOne;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

@Table(name = "absence")

public class Absence extends AbstractEntity {
	@OneToOne(mappedBy = "user")
	private Integer userId; //Foreign Key for user table
	private String absenceReason;
	private String authorisedBy; 
	private Integer documentsId; //Foreign key for documents table
	private Boolean payauthorisation; //Amount
        private LocalDateTime dateModified;
        private LocalDate startDate;
        private LocalDate endDate;
        private Boolean absenceApproval;
        

	public Integer userId() { 
	return userId; 
	}

	public void setuserId(Integer userId) { 
	this.userId = userId; 
	}

	public String absenceReason() { 
	return absenceReason; 
	}

	public void setabsenceReason(String absenceReason) { 
	this.absenceReason = absenceReason; 
	}
	public String authorisedBy() { 
	return authorisedBy; 
	}

	public void setauthorisedBy(String authorisedBy) { 
	this.authorisedBy = authorisedBy; 
	}
	public Integer documentsId() { 
	return documentsId; 
	}

	public void setDocumentsId(Integer documentsId) { 
	this.documentsId = documentsId; 
	}
	public Boolean payauthorisation() { 
	return payauthorisation; 
	}

	public void setpayauthorisation(Boolean payauthorisation) { 
	this.payauthorisation = payauthorisation; 
	}
	 public LocalDateTime getDateModified() {
         return dateModified;
     }
    @PreUpdate 
	public void updateDateModified() {
     this.dateModified = LocalDateTime.now();
 }

    	public LocalDate getstartDate() {
        return startDate;
    }
    public void setstartDate(LocalDate startDate ) {
        this.startDate = startDate;
    }
    public LocalDate getSendDate() {
        return endDate;
    }
    public void setendDate(LocalDate endDate ) {
        this.endDate = endDate;
    }
    public Boolean getabsenceApproval() {
        return absenceApproval;
    }
    public void setabsenceApproval(Boolean absenceApproval ) {
        this.absenceApproval = absenceApproval;
    }
}
