package com.iamin.data.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.iamin.data.entity.Events;
import com.iamin.data.entity.SamplePerson;
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
}