package com.iamin.data.service;
import com.iamin.data.entity.Holidays;
import com.iamin.data.entity.SamplePerson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface HolidaysRepository extends JpaRepository<Holidays, Long> {

    List<Holidays> findByPerson(SamplePerson person);

    @Query("SELECT SUM(h.totalDays) FROM Holidays h WHERE h.person = :person AND h.holidaysApproval = true OR h.holidaysApproval = NULL") 
    Integer calculateTotalDaysOff(@Param("person") SamplePerson person);
    @Query("SELECT h FROM Holidays h WHERE h.holidaysApproval IS NULL")
    List<Holidays> findAllUnapproved();

}
