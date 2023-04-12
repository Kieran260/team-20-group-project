package com.iamin.data.entity;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "events")
public class Events extends AbstractEntity {

    private String eventTitle;
    private String eventDescription;
    private LocalDate eventDate;
    private LocalDateTime eventTime;
    private String eventType;
    private String eventLocation;

    
    @ManyToMany
    @JoinTable(
            name = "event_person",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "person_id")
    )
    private List<SamplePerson> attendees = new ArrayList<>();

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public LocalDate getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }

    public LocalDateTime getEventTime() {
        return eventTime;
    }

    public void setEventTime(LocalDateTime eventTime) {
        this.eventTime = eventTime;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public List<SamplePerson> getAttendees() {
        return attendees;
    }

    public void setAttendees(List<SamplePerson> attendees) {
        this.attendees = attendees;
    }

    public void addAttendee(SamplePerson attendee) {
        this.attendees.add(attendee);
    }

    public void removeAttendee(SamplePerson attendee) {
        this.attendees.remove(attendee);
    }
    
    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

}
