package com.iamin.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.iamin.data.Role;

import java.time.LocalDateTime;
import java.util.Set;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

@Entity
@Table(name = "login")
public class Login extends AbstractEntity  {
	
	@ManyToOne
	@JoinColumn(name = "person_id")
	private SamplePerson person;
    private String username;
    @JsonIgnore
    private String hashedPassword;
    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Role> role;    
    private LocalDateTime datemodified;
    
    
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public LocalDateTime getDatemodified() {
		return datemodified;
    	
    }

    @PreUpdate
    public void updateDatemodified() {
    	this.datemodified = LocalDateTime.now();
    }
    public String getHashedPassword() {
        return hashedPassword;
    }
    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }
    public Set<Role> getRoles() {
        return role;
    }
    public void setRoles(Set<Role> role) {
        this.role = role;
    }
    
    public SamplePerson getPerson() {
        return person;
    }

    public void setPerson(SamplePerson person) {
        this.person = person;
    }
    

}