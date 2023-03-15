package com.iamin.data.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;




@Entity
@Table(name = "checkInOut")
public class CheckInOut extends AbstractEntity {
	
	@ManyToOne
	@JoinColumn(name = "person_id")
	private SamplePerson person;
    private LocalTime checkInTime;
    private LocalTime checkOutTime;
    private LocalDate date;

    

    public LocalTime getcheckInTime() {
        return checkInTime;
    }
    public void setcheckInTime(LocalTime checkInTime) {
        this.checkInTime = LocalTime.now();
    }
    public LocalTime getcheckOutTime() {
        return checkOutTime;
    }
    public void setcheckOutTime(LocalTime checkOutTime) {
        this.checkOutTime = LocalTime.now();
    }
    public LocalDate getdate() {
        return date;
    }
    public void setdate(LocalDate date) {
        this.date = LocalDate.now();
    }
    public SamplePerson getPerson() {
        return person;
    }

    public void setPerson(SamplePerson person) {
        this.person = person;
    }
}