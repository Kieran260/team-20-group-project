package com.iamin.data.entity;
import java.time.LocalDateTime;
import javax.persistence.*;

public class PaymentInfo extends AbstractEntity{
  
	@OneToOne(mappedBy = "user")
	private Integer user_ID; //Foreign Key for user table
        private LocalDateTime dateModified;
        private String expiryDate; //Not sure if integer due to MM/YY the slash 
	private Integer accountNumber;
	private String sortCode;
        
        
	

	public Integer user_ID() { 
	return user_ID; 
	}

	public void setuser_ID(Integer user_ID) { 
	this.user_ID = user_ID; 
	}

	 public LocalDateTime getDateModified() {
         return dateModified;
     }
    @PreUpdate 
	public void updateDateModified() {
     this.dateModified = LocalDateTime.now();
 }

	public String getexpiryDate() {
        return expiryDate;
    }
    public void setexpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }
	
	public Integer getaccountNumber() {
        return accountNumber;
    }
    public void setaccountNumber(Integer accountNumber) {
        this.accountNumber = accountNumber;
    }
	public String getsortCode() {
        return sortCode;
    }
    public void setsortCode(String sortCode) {
        this.sortCode = sortCode;
    }
	
}

