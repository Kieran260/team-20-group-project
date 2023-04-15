package com.iamin.data.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.iamin.data.entity.Events;
import com.iamin.data.entity.SamplePerson;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class EventService {

    private final EventsRepository eventRepository;

    @Autowired
    public EventService(EventsRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Events createEvent(Events event) {
        return eventRepository.save(event);
    }

    public Optional<Events> getEventById(Long eventId) {
        return eventRepository.findById(eventId);
    }

    public List<Events> getAllEvents() {
        return eventRepository.findAll();
    }

    public Events updateEvent(Events event) {
        return eventRepository.save(event);
    }

    
    public void deleteEvent(Long eventId) {
        eventRepository.deleteById(eventId);
    }    

    public List<SamplePerson> getAttendees(Long eventId) {
        Optional<Events> optionalEvent = eventRepository.findById(eventId);
        return optionalEvent.map(Events::getAttendees).orElse(null);
    }

    public List<Events> findByAttendees_Id(Long personId) {
        return eventRepository.findByAttendees_Id(personId);
    }

    public Page<Events> list(Pageable pageable) {
        return eventRepository.findAll(pageable);
    }

    public List<Events> findEventsWithinHoursForPerson(SamplePerson person, int hours) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime upperBound = now.plusHours(hours);
    
        List<Events> eventsForPerson = findByAttendees_Id(person.getId());
        List<Events> eventsWithinHours = new ArrayList<>();
        for (Events event : eventsForPerson) {
            LocalDateTime eventDateTime = event.getEventDate().atTime(event.getEventTime());
            if (eventDateTime.isAfter(now) && eventDateTime.isBefore(upperBound)) {
                eventsWithinHours.add(event);
            }
        }
    
        return eventsWithinHours;
    }    

    public List<Events> findEventsForDate(SamplePerson person, LocalDate date) {
        List<Events> eventsForPerson = findByAttendees_Id(person.getId());
        List<Events> eventsForDate = new ArrayList<>();
        for (Events event : eventsForPerson) {
            if (event.getEventDate().equals(date)) {
                eventsForDate.add(event);
            }
        }
    
        return eventsForDate;
    }

    public List<Events> findEventsByDate(LocalDate date) {
        return eventRepository.findByEventDate(date);
    }

    public List<Events> findEventsToday() {
        LocalDate today = LocalDate.now();
        return eventRepository.findByEventDate(today);
    }

    public List<Events> findEventsTodayForPerson(SamplePerson person) {
        LocalDate today = LocalDate.now();
        List<Events> eventsForPerson = findByAttendees_Id(person.getId());
        List<Events> eventsToday = new ArrayList<>();
        for (Events event : eventsForPerson) {
            if (event.getEventDate().equals(today)) {
                eventsToday.add(event);
            }
        }
        return eventsToday;
    }
    
}