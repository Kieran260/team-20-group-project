package com.iamin.data.service;
import com.iamin.data.entity.Holidays;
import com.iamin.data.entity.SamplePerson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HolidaysRepository extends JpaRepository<Holidays, Long> {

    List<Holidays> findByPerson(SamplePerson person);

    @Query("SELECT SUM(h.totalDays) FROM Holidays h WHERE h.person = :person")
    Integer calculateTotalDaysOff(@Param("person") SamplePerson person);

}
