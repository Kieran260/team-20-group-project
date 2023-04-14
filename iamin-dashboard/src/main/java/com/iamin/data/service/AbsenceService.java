package com.iamin.data.service;

import com.iamin.data.entity.Absence;
import com.iamin.data.entity.SamplePerson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AbsenceService {

    private final AbsenceRepository absenceRepository;

    @Autowired
    public AbsenceService(AbsenceRepository absenceRepository) {
        this.absenceRepository = absenceRepository;
    }

    public List<Absence> getAllAbsences() {
        return absenceRepository.findAll();
    }

    public List<Absence> getAbsencesForPerson(SamplePerson person) {
        return absenceRepository.findByPerson(person);
    }

    public Absence createAbsenceRequest(Absence absenceRequest) {
        return absenceRepository.save(absenceRequest);
    }

    public void deleteAbsenceRequest(Absence absenceRequest) {
        absenceRepository.delete(absenceRequest);
    }
    public List<Absence> findAllUnapproved() {
        return absenceRepository.findAllUnapproved();
    }

    public Optional<Absence> findById(Long requestId) {
        return absenceRepository.findById(requestId);
    }
}
