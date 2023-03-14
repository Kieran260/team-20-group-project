
package com.iamin.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.iamin.data.Role;

import java.time.LocalDateTime;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "login")
public class Login extends AbstractEntity {
	
//	public Login(String login_id ,String User_id , String username , String hashedPassword , Set<Role> role , String datamodified ){
//		setUsername(username);
//		setDatamodified(datamodified);
//		setHashedPassword(hashedPassword);
//		setRole(role);
//		setUser_id(User_id);
//		setLogin_id(login_id);
//		
//	}
	
	
    private String login_id;
    private String User_id;
    private String username;
    @JsonIgnore
    private String hashedPassword;
   
    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Role> role;
    
    
  
    private String datamodified;
    
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getDatamodified() {
		return datamodified;
    	
    }
    public void setLogin_id(String login_id) {
    	this.login_id = login_id;
    }
    public void setUser_id(String User_id) {
    	this.User_id = User_id;
    }
    public void setDatamodified(String datamodified) {
    	this.datamodified = datamodified;
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
