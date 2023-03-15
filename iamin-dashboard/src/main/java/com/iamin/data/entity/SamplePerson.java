package com.iamin.data.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PreUpdate;
import javax.validation.constraints.Email;

@Entity
public class SamplePerson extends AbstractEntity {

    private String firstName;
    private String lastName;
    @Email
    private String email;
    private String phone;
    private LocalDate dateOfBirth;
    private String address;
    private String occupation;
    private String jobTitle;
    private LocalDateTime dateModified;
    private Integer maxHolidays;
    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

   

    public Department getDepartment() {
        return department;
    }
    public void setDepartment(Department department) {
        this.department = department;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }
    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public String getOccupation() {
        return occupation;
    }
    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }
    public String getjobTitle() {
        return jobTitle;
    }
    public void setjobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }
    public LocalDateTime getDateModified() {
        return dateModified;
    }
    @PreUpdate
    public void updateDateModified() {
    	this.dateModified = LocalDateTime.now();
    }

	public Integer getmaxHolidays() {
        return maxHolidays;
    }
    public void setmaxHolidays(Integer maxHolidays) {
        this.maxHolidays = maxHolidays;
    }



}