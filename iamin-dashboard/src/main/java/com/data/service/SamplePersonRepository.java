package com..data.service;

import com..data.entity.SamplePerson;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import javax.validation.constraints.Email;
import java.time.LocalDate;

public interface SamplePersonRepository extends JpaRepository<SamplePerson, Long>, JpaSpecificationExecutor<SamplePerson> {

    
}
