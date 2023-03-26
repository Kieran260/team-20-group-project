package com.iamin.data.service;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import com.iamin.data.entity.Absence;
import com.iamin.data.entity.SamplePerson;

public interface AbsenceRepository extends JpaRepository<Absence, Long>, JpaSpecificationExecutor<Absence> {

    List<Absence> findByPerson(SamplePerson person);
   
}

