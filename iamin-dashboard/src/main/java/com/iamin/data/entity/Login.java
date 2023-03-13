
package com.iamin.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.iamin.data.Role;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

@Entity
@Table(name = "login")
public class Login extends AbstractEntity  {
	
	
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
    public Set<Role> getRole() {
        return role;
    }
    public void setRole(Set<Role> role) {
        this.role = role;
    }
    
   

}
