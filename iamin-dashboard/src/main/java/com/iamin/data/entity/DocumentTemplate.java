package com.iamin.data.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

 @Table(name = "DocumentTemplate")

public class DocumentTemplate extends AbstractEntity {

	private String type; 
        private LocalDate addedDate;
        private String references;
        private String title;
        private String description;
        private String modifiedBy;
       @Column(length = 1000000)
        private byte[] attachment;
        private LocalDateTime dateModified;
  
        
       
	public String gettype() { 
	return type; 
	}

	public void settype(String type) { 
	this.type = type; 
	}
        public void setaddedDate(LocalDate addedDate ) {
        this.addedDate = addedDate;
        }
        public LocalDate getaddedDate() {
            return addedDate;
        }
        public String getreferences() { 
	return references; 
	}

	public void setreferences(String references) { 
	this.references = references; 
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
        public String getmodifiedBy() { 
	return modifiedBy; 
	}

	public void setmodifiedBy(String modifiedBy) { 
	this.modifiedBy = modifiedBy; 
	}
	 public LocalDateTime getDateModified() {
         return dateModified;
     }
    @PreUpdate 
	public void updateDateModified() {
     this.dateModified = LocalDateTime.now();
 }
 
     public byte[] getattachment() {
        return attachment;
    }
    public void setattachment(byte[] attachment) {
        this.attachment = attachment;
    }
}
 